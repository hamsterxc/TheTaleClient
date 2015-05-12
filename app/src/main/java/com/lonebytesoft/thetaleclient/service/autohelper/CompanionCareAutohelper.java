package com.lonebytesoft.thetaleclient.service.autohelper;

import com.lonebytesoft.thetaleclient.sdk.dictionary.HeroAction;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 24.02.2015
 */
public class CompanionCareAutohelper implements Autohelper {

    @Override
    public boolean shouldHelp(GameInfoResponse gameInfoResponse) {
        return (gameInfoResponse.account.hero.action.type == HeroAction.COMPANION_CARE)
                && (gameInfoResponse.account.hero.companionInfo.healthCurrent < PreferencesManager.getAutohelpCompanionCareHealthAmountThreshold())
                && PreferencesManager.shouldAutohelpCompanionCare()
                && GameInfoUtils.isEnoughEnergy(
                gameInfoResponse.account.hero.energy,
                PreferencesManager.getAutohelpCompanionCareEnergyThreshold(),
                PreferencesManager.shouldAutohelpCompanionCareUseBonusEnergy(),
                PreferencesManager.getAutohelpCompanionCareBonusEnergyThreshold());
    }

}
