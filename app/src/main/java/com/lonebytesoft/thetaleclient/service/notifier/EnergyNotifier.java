package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class EnergyNotifier {

    private int lastNotify = -1;

    public void processState(final int energy) {
        if(PreferencesManager.shouldNotifyEnergy()) {
            if (energy >= PreferencesManager.getNotificationThresholdEnergy()) {
                if (energy != lastNotify) {
                    lastNotify = energy;
                    NotificationUtils.notifyHighEnergy(energy);
                }
            }
        }
    }

}
