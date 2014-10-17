package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.GameStateWatcher;
import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class EnergyNotifier implements GameStateWatcher {

    private int lastNotify = -1;

    @Override
    public void processGameState(GameInfoResponse gameInfoResponse) {
        if(PreferencesManager.shouldNotifyEnergy()) {
            final int energy = gameInfoResponse.account.hero.energy.current;
            if (energy >= PreferencesManager.getNotificationThresholdEnergy()) {
                if (energy != lastNotify) {
                    lastNotify = energy;
                    NotificationUtils.notifyHighEnergy(energy);
                }
            }
        }
    }

}
