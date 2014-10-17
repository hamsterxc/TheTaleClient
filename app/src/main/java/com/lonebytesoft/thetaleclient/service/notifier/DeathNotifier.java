package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.GameStateWatcher;
import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
* @author Hamster
* @since 10.10.2014
*/
public class DeathNotifier implements GameStateWatcher {

    private boolean isNotified = false;

    @Override
    public void processGameState(GameInfoResponse gameInfoResponse) {
        if(PreferencesManager.shouldNotifyDeath()) {
            if (gameInfoResponse.account.hero.basicInfo.isAlive) {
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
