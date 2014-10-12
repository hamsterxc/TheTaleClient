package com.lonebytesoft.thetaleclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.QuestType;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.notifier.DeathNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.EnergyNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.HealthNotifier;
import com.lonebytesoft.thetaleclient.service.notifier.IdlenessNotifier;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class NotificationService extends Service {

    private static final long REQUEST_TIMEOUT_MILLIS = 10000; // 10 s

    private final Handler handler = new Handler();
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                @Override
                public void processResponse(GameInfoResponse response) {
                    if(response.account == null) {
                        stopSelf();
                        return;
                    }

                    deathNotifier.processState(response.account.hero.basicInfo.isAlive);
                    idlenessNotifier.processState(response.account.hero.quests.get(response.account.hero.quests.size() - 1).get(0).type == QuestType.NO_QUEST);
                    healthNotifier.processState(response.account.hero.basicInfo.healthCurrent);
                    energyNotifier.processState(response.account.hero.energy.current);

                    handler.postDelayed(refreshRunnable, REQUEST_TIMEOUT_MILLIS);
                }

                @Override
                public void processError(GameInfoResponse response) {
                    handler.postDelayed(refreshRunnable, REQUEST_TIMEOUT_MILLIS);
                }
            });
        }
    };

    private DeathNotifier deathNotifier;
    private IdlenessNotifier idlenessNotifier;
    private HealthNotifier healthNotifier;
    private EnergyNotifier energyNotifier;

    @Override
    public void onCreate() {
        deathNotifier = new DeathNotifier();
        idlenessNotifier = new IdlenessNotifier();
        healthNotifier = new HealthNotifier();
        energyNotifier = new EnergyNotifier();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        refreshRunnable.run();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
