package com.lonebytesoft.thetaleclient.service.notifier;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.service.GameStateWatcher;
import com.lonebytesoft.thetaleclient.util.NotificationUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 21.10.2014
 */
public class NewMessagesNotifier implements GameStateWatcher {

    private int lastNotify = 0;

    @Override
    public void processGameState(GameInfoResponse gameInfoResponse) {
        if(PreferencesManager.shouldNotifyNewMessages()) {
            final int newMessages = gameInfoResponse.account.newMessagesCount;
            if ((newMessages > 0) && (newMessages != lastNotify)) {
                NotificationUtils.notifyNewMessages(newMessages);
            }
            lastNotify = newMessages;
        }
    }

}
