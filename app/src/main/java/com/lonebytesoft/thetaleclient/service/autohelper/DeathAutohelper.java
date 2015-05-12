package com.lonebytesoft.thetaleclient.service.autohelper;

import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public class DeathAutohelper implements Autohelper {

    @Override
    public boolean shouldHelp(GameInfoResponse gameInfoResponse) {
        return !gameInfoResponse.account.hero.basicInfo.isAlive
                && PreferencesManager.shouldAutohelpDeath()
                && GameInfoUtils.isEnoughEnergy(
                    gameInfoResponse.account.hero.energy,
                    PreferencesManager.getAutohelpDeathEnergyThreshold(),
                    PreferencesManager.shouldAutohelpDeathUseBonusEnergy(),
                    PreferencesManager.getAutohelpDeathBonusEnergyThreshold());
    }

}
