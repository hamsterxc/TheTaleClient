package com.lonebytesoft.thetaleclient.util;

import android.content.Context;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.dictionary.QuestType;
import com.lonebytesoft.thetaleclient.api.model.EnergyInfo;
import com.lonebytesoft.thetaleclient.api.model.HeroActionInfo;
import com.lonebytesoft.thetaleclient.api.model.QuestStepInfo;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

import java.util.List;

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

    public static boolean isEnoughEnergy(final EnergyInfo energyInfo, final int need) {
        return (energyInfo.current + energyInfo.bonus - energyInfo.discount) >= need;
    }

    public static boolean isHeroIdle(final GameInfoResponse gameInfoResponse) {
        return gameInfoResponse.account.hero.quests.get(gameInfoResponse.account.hero.quests.size() - 1).get(0).type == QuestType.NO_QUEST;
    }

    public static boolean isQuestChoiceAvailable(final GameInfoResponse gameInfoResponse) {
        final int questLinesCount = gameInfoResponse.account.hero.quests.size();
        if(questLinesCount > 0) {
            final List<QuestStepInfo> lastQuestLine = gameInfoResponse.account.hero.quests.get(questLinesCount - 1);
            final int questStepsCount = lastQuestLine.size();
            if(questStepsCount > 0) {
                final QuestStepInfo lastQuestStep = lastQuestLine.get(questStepsCount - 1);
                return lastQuestStep.choices.size() > 0;
            }
        }
        return false;
    }

    public static String getEnergyString(final EnergyInfo energyInfo) {
        return String.format("%d/%d + %d", energyInfo.current, energyInfo.max, energyInfo.bonus);
    }

    public static String getActionString(final Context context, final HeroActionInfo actionInfo) {
        String actionDescription = actionInfo.description;
        if(actionInfo.isBossFight) {
            actionDescription += context.getString(R.string.game_boss_fight);
        }
        return actionDescription;
    }

}
