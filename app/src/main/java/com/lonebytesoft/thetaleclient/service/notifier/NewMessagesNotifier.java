package com.lonebytesoft.thetaleclient.service.notifier;

import android.app.PendingIntent;
import android.content.Context;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenPart;

/**
 * @author Hamster
 * @since 08.12.2014
 */
public class NewMessagesNotifier implements Notifier {

    private GameInfoResponse gameInfoResponse;

    @Override
    public void setInfo(GameInfoResponse gameInfoResponse) {
        this.gameInfoResponse = gameInfoResponse;
    }

    @Override
    public boolean isNotifying() {
        final int value = getValue();
        PreferencesManager.setLastNotificationNewMessages(value);
        if((value > 0) && (value != PreferencesManager.getLastShownNotificationNewMessages())) {
            if(PreferencesManager.shouldNotifyNewMessages()
                    && PreferencesManager.shouldShowNotificationNewMessages()
                    && !TheTaleClientApplication.getOnscreenStateWatcher().isOnscreen(OnscreenPart.GAME_INFO)) {
                return true;
            }
            PreferencesManager.setShouldShowNotificationNewMessages(false);
        } else {
            PreferencesManager.setShouldShowNotificationNewMessages(true);
        }
        return false;
    }

    @Override
    public String getNotification(Context context) {
        return context.getString(R.string.notification_new_messages, getValue());
    }

    private int getValue() {
        return gameInfoResponse.account.newMessagesCount;
    }

    @Override
    public PendingIntent getPendingIntent(Context context) {
        return UiUtils.getMainActivityIntent(context, GameFragment.GamePage.GAME_INFO);
    }

    @Override
    public void onNotificationDelete() {
        PreferencesManager.setShouldShowNotificationNewMessages(false);
        PreferencesManager.setLastShownNotificationNewMessages(PreferencesManager.getLastNotificationNewMessages());
    }

}
