package com.lonebytesoft.thetaleclient.service.autohelper;

import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public class IdlenessAutohelper implements Autohelper {

    @Override
    public boolean shouldHelp(GameInfoResponse gameInfoResponse) {
        return GameInfoUtils.isHeroIdle(gameInfoResponse)
                && PreferencesManager.shouldAutohelpIdle()
                && GameInfoUtils.isEnoughEnergy(
                    gameInfoResponse.account.hero.energy,
                    PreferencesManager.getAutohelpIdleEnergyThreshold(),
                    PreferencesManager.shouldAutohelpIdleUseBonusEnergy(),
                    PreferencesManager.getAutohelpIdleBonusEnergyThreshold());
    }

}
