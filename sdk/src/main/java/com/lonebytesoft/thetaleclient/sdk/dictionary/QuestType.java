package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum QuestType {

    CARAVAN("caravan"),
    DELIVERY("delivery"),
    HELP("help"),
    HELP_FRIEND("help_friend"),
    HOMETOWN("hometown"),
    HUNT("hunt"),
    ENEMY("interfere_enemy"),
    DEBT("collect_debt"),
    SPYING("spying"),
    SMITH("search_smith"),
    NO_QUEST("no-quest"),
    SPENDING("next-spending"),
    PILGRIMAGE("pilgrimage"),
    ;

    public final String code;
    public final String name;

    QuestType(final String code) {
        this.code = code;
        this.name = code;
    }

}
