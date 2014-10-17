package com.lonebytesoft.thetaleclient.util;

import com.lonebytesoft.thetaleclient.api.dictionary.QuestType;
import com.lonebytesoft.thetaleclient.api.model.EnergyInfo;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public class GameInfoUtils {

    public static boolean isEnoughEnergy(final EnergyInfo energyInfo, final int energyThreshold,
                                         final boolean shouldUseBonusEnergy, final int bonusEnergyThreshold) {
        return (energyInfo.current >= energyThreshold)
                || shouldUseBonusEnergy && (energyInfo.bonus >= bonusEnergyThreshold);
    }

    public static boolean isHeroIdle(final GameInfoResponse gameInfoResponse) {
        return gameInfoResponse.account.hero.quests.get(gameInfoResponse.account.hero.quests.size() - 1).get(0).type == QuestType.NO_QUEST;
    }

}
