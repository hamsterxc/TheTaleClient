package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class IdlenessNotifier {

    private boolean isNotified = false;

    public void processState(final boolean isIdle) {
        if(PreferencesManager.shouldNotifyIdleness()) {
            if (isIdle) {
                if (!isNotified) {
                    isNotified = true;
                    NotificationUtils.notifyIdleness();
                }
            } else {
                isNotified = false;
            }
        }
    }

}
