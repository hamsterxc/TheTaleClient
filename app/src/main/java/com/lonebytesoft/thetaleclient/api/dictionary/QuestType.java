package com.lonebytesoft.thetaleclient.api.dictionary;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum QuestType {

    CARAVAN("caravan", R.drawable.quest_caravan),
    DELIVERY("delivery", R.drawable.quest_delivery),
    HELP("help", R.drawable.quest_help),
    HELP_FRIEND("help_friend", R.drawable.quest_help_friend),
    HOMETOWN("hometown", R.drawable.quest_hometown),
    HUNT("hunt", R.drawable.quest_hunt),
    ENEMY("interfere_enemy", R.drawable.quest_enemy),
    DEBT("collect_debt", R.drawable.quest_debt),
    SPYING("spying", R.drawable.quest_spying),
    SMITH("search_smith", R.drawable.quest_smith),
    NO_QUEST("no-quest", R.drawable.quest_no_quest),
    SPENDING("next-spending", R.drawable.quest_next_spending),
    PILGRIMAGE("pilgrimage", R.drawable.quest_pilgrimage),
    ;

    private final String code;
    private final int drawableResId;

    private QuestType(final String code, final int drawableResId) {
        this.code = code;
        this.drawableResId = drawableResId;
    }

    public String getCode() {
        return code;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

}
