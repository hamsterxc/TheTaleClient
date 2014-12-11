package com.lonebytesoft.thetaleclient.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;

import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.request.AbilityUseRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.autohelper.Autohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.DeathAutohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.EnergyAutohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.HealthAutohelper;
import com.lonebytesoft.thetaleclient.service.autohelper.IdlenessAutohelper;
import com.lonebytesoft.thetaleclient.service.watcher.CardTaker;
import com.lonebytesoft.thetaleclient.service.watcher.GameStateWatcher;
import com.lonebytesoft.thetaleclient.util.NotificationManager;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class WatcherService extends Service {

    private static final long REQUEST_TIMEOUT_MILLIS = 10000; // 10 s

    private final Handler handler = new Handler();
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if(PreferencesManager.isWatcherEnabled()) {
                new GameInfoRequest(false).execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(GameInfoResponse response) {
                        if (response.account == null) {
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

                        postRefresh();
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
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

    private void postRefresh() {
        handler.postDelayed(refreshRunnable, REQUEST_TIMEOUT_MILLIS);
    }

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeCallbacks(refreshRunnable);
        refreshRunnable.run();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
