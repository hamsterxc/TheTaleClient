package com.lonebytesoft.thetaleclient.service.notifier;

import android.app.PendingIntent;
import android.content.Context;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.onscreen.OnscreenPart;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 08.12.2014
 */
public class EnergyNotifier implements Notifier {

    private GameInfoResponse gameInfoResponse;

    @Override
    public void setInfo(GameInfoResponse gameInfoResponse) {
        this.gameInfoResponse = gameInfoResponse;
    }

    @Override
    public boolean isNotifying() {
        final int value = getValue();
        PreferencesManager.setLastNotificationEnergy(value);
        if((value >= PreferencesManager.getNotificationThresholdEnergy()) && (value != PreferencesManager.getLastShownNotificationEnergy())) {
            if(PreferencesManager.shouldNotifyEnergy()
                    && PreferencesManager.shouldShowNotificationEnergy()
                    && !TheTaleClientApplication.getOnscreenStateWatcher().isOnscreen(OnscreenPart.GAME_INFO)) {
                return true;
            }
            PreferencesManager.setShouldShowNotificationEnergy(false);
        } else {
            PreferencesManager.setShouldShowNotificationEnergy(true);
        }
        return false;
    }

    @Override
    public String getNotification(Context context) {
        return context.getString(R.string.notification_high_energy, getValue());
    }

    private int getValue() {
        return gameInfoResponse.account.hero.energy.current;
    }

    @Override
    public PendingIntent getPendingIntent(Context context) {
        return UiUtils.getMainActivityIntent(context, GameFragment.GamePage.GAME_INFO);
    }

    @Override
    public void onNotificationDelete() {
        PreferencesManager.setShouldShowNotificationEnergy(false);
        PreferencesManager.setLastShownNotificationEnergy(PreferencesManager.getLastNotificationEnergy());
    }

}
