package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class HealthNotifier {

    private boolean isNotified = false;

    public void processState(final int health) {
        if(PreferencesManager.shouldNotifyHealth()) {
            if (health < PreferencesManager.getNotificationThresholdHealth()) {
                if (!isNotified) {
                    isNotified = true;
                    NotificationUtils.notifyLowHealth(health);
                }
            } else {
                isNotified = false;
            }
        }
    }

}
