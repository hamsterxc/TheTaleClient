package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class DeathNotifier {

    private boolean isNotified = false;

    public void processState(final boolean isAlive) {
        if(PreferencesManager.shouldNotifyDeath()) {
            if (isAlive) {
                isNotified = false;
            } else {
                if (!isNotified) {
                    isNotified = true;
                    NotificationUtils.notifyDeath();
                }
            }
        }
    }

}
