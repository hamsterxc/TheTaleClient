package com.lonebytesoft.thetaleclient.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.model.DiaryEntry;
import com.lonebytesoft.thetaleclient.api.request.AbilityUseRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.autohelper.Autohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.DeathAutohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.EnergyAutohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.HealthAutohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.IdlenessAutohelper;
import com.lonebytesoft.thetaleclient.service.watcher.CardTaker;
import com.lonebytesoft.thetaleclient.service.watcher.GameStateWatcher;
import com.lonebytesoft.thetaleclient.service.widget.AppWidgetHelper;
import com.lonebytesoft.thetaleclient.util.NotificationManager;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.TextToSpeechUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenPart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class WatcherService extends Service {

    public static final String BROADCAST_SERVICE_RESTART_REFRESH_ACTION =
            TheTaleClientApplication.getContext().getPackageName() + ".service.restart.refresh";

    private static final double INTERVAL_MULTIPLIER = (Math.sqrt(5.0) + 1) / 2.0; // phi
    private static final long INTERVAL_MAX = 600;
    private double intervalMultiplierCurrent;

    private final Handler handler = new Handler();
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if(PreferencesManager.isWatcherEnabled()) {
                new GameInfoRequest(false).execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(GameInfoResponse response) {
                        if (response.account == null) {
                            AppWidgetHelper.updateWithError(WatcherService.this, getString(R.string.game_not_authorized));
                            stopSelf();
                            return;
                        }

                        TheTaleClientApplication.getNotificationManager().notify(response);

                        for (final GameStateWatcher watcher : watchers) {
                            watcher.processGameState(response);
                        }

                        boolean shouldHelp = false;
                        for (final Autohelper autohelper : autohelpers) {
                            shouldHelp |= autohelper.shouldHelp(response);
                            if (shouldHelp) {
                                break;
                            }
                        }
                        if (shouldHelp) {
                            new AbilityUseRequest(Action.HELP).execute(0, null);
                        }

                        final int diarySize = response.account.hero.diary.size();
                        final int lastDiaryTimestamp = PreferencesManager.getLastDiaryEntryRead();
                        if(PreferencesManager.isDiaryReadAloudEnabled()
                                && TheTaleClientApplication.getOnscreenStateWatcher().isOnscreen(OnscreenPart.MAIN)
                                && (lastDiaryTimestamp > 0)) {
                            for(int i = 0; i < diarySize; i++) {
                                final DiaryEntry diaryEntry = response.account.hero.diary.get(i);
                                if(diaryEntry.timestamp > lastDiaryTimestamp) {
                                    TextToSpeechUtils.speak(String.format("%s, %s.\n%s",
                                            diaryEntry.date, diaryEntry.time, diaryEntry.text));
                                }
                            }
                        }
                        PreferencesManager.setLastDiaryEntryRead(response.account.hero.diary.get(diarySize - 1).timestamp);

                        AppWidgetHelper.update(WatcherService.this, DataViewMode.DATA, response);

                        intervalMultiplierCurrent = 1.0;
                        postRefresh();
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                        AppWidgetHelper.update(WatcherService.this, DataViewMode.ERROR, response);

                        intervalMultiplierCurrent *= INTERVAL_MULTIPLIER;
                        postRefresh();
                    }
                }, false);
            } else {
                postRefresh();
            }
        }
    };

    private final BroadcastReceiver notificationDeleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(NotificationManager.BROADCAST_NOTIFICATION_DELETE_ACTION.equals(intent.getAction())) {
                TheTaleClientApplication.getNotificationManager().onNotificationDelete();
            }
        }
    };
    private final BroadcastReceiver widgetHelpReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if(AppWidgetHelper.BROADCAST_WIDGET_HELP_ACTION.equals(intent.getAction())) {
                AppWidgetHelper.update(context, DataViewMode.LOADING, null);
                new AbilityUseRequest(Action.HELP).execute(0, new ApiResponseCallback<CommonResponse>() {
                    @Override
                    public void processResponse(CommonResponse response) {
                        AppWidgetHelper.updateWithRequest(context);
                    }

                    @Override
                    public void processError(CommonResponse response) {
                        AppWidgetHelper.updateWithError(context, response.errorMessage);
                    }
                });
            }
        }
    };
    private final BroadcastReceiver restartRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BROADCAST_SERVICE_RESTART_REFRESH_ACTION.equals(intent.getAction())) {
                restartRefresh();
            }
        }
    };
    private final BroadcastReceiver refreshWidgetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(AppWidgetHelper.BROADCAST_WIDGET_REFRESH_ACTION.equals(intent.getAction())) {
                AppWidgetHelper.update(context, DataViewMode.LOADING, null);
                restartRefresh();
            }
        }
    };

    private List<GameStateWatcher> watchers;
    private List<Autohelper> autohelpers;

    @Override
    public void onCreate() {
        watchers = new ArrayList<>();
        watchers.add(new CardTaker());

        autohelpers = new ArrayList<>();
        autohelpers.add(new DeathAutohelper());
        autohelpers.add(new IdlenessAutohelper());
        autohelpers.add(new HealthAutohelper());
        autohelpers.add(new EnergyAutohelper());

        registerReceiver(notificationDeleteReceiver, new IntentFilter(NotificationManager.BROADCAST_NOTIFICATION_DELETE_ACTION));
        registerReceiver(widgetHelpReceiver, new IntentFilter(AppWidgetHelper.BROADCAST_WIDGET_HELP_ACTION));
        registerReceiver(restartRefreshReceiver, new IntentFilter(BROADCAST_SERVICE_RESTART_REFRESH_ACTION));
        registerReceiver(refreshWidgetReceiver, new IntentFilter(AppWidgetHelper.BROADCAST_WIDGET_REFRESH_ACTION));

        intervalMultiplierCurrent = 1.0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        restartRefresh();
        return START_STICKY;
    }

    private void postRefresh() {
        final int intervalInitial = PreferencesManager.getServiceInterval();
        long interval;
        if(intervalInitial >= INTERVAL_MAX) {
            interval = intervalInitial;
            intervalMultiplierCurrent /= INTERVAL_MULTIPLIER;
        } else {
            interval = (long) Math.floor(intervalInitial * intervalMultiplierCurrent);
            if(interval > INTERVAL_MAX) {
                intervalMultiplierCurrent = ((double) INTERVAL_MAX) / ((double) intervalInitial);
                interval = INTERVAL_MAX;
            }
        }

        handler.postDelayed(refreshRunnable, interval * 1000);
    }

    private void restartRefresh() {
        handler.removeCallbacks(refreshRunnable);
        refreshRunnable.run();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
