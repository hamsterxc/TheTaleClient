package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public enum Action {

    HELP("help"),
    ARENA_GO("arena_pvp_1x1"),
    ARENA_LEAVE("arena_pvp_1x1_leave_queue"),
    ARENA_ACCEPT("arena_pvp_1x1_accept"),
    BUILDING_REPAIR("building_repair"),
    DROP_ITEM("drop_item"),
    ;

    private final String code;

    private Action(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
