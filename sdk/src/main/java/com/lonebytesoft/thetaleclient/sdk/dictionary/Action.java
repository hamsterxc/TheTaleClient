package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public enum Action {

    HELP("help", "Помочь"),
    ARENA_GO("arena_pvp_1x1", "Отправить на арену"),
    ARENA_LEAVE("arena_pvp_1x1_leave_queue", "Выйти из очереди"),
    ARENA_ACCEPT("arena_pvp_1x1_accept", "Принять вызов"),
    BUILDING_REPAIR("building_repair", "Вызвать рабочего"),
    DROP_ITEM("drop_item", "Выбросить предмет"),
    ;

    public final String code;
    public final String name;

    Action(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

}
