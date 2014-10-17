package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.GameStateWatcher;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class IdlenessNotifier implements GameStateWatcher {

    private boolean isNotified = false;

    @Override
    public void processGameState(GameInfoResponse gameInfoResponse) {
        if(PreferencesManager.shouldNotifyIdleness()) {
            if (GameInfoUtils.isHeroIdle(gameInfoResponse)) {
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
