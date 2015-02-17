package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum HeroAction {

    IDLE(0),
    QUEST(1),
    TRAVEL(2),
    BATTLE(3),
    RESURRECTION(4),
    TOWN(5),
    REST(6),
    EQUIP(7),
    TRADE(8),
    NEAR_TOWN(9),
    RELIGIOUS(10),
    NO_EFFECT(11),
    PROXY_HEROES(12),
    PVP(13),
    TEST(14),
    COMPANION_CARE(15),
    ;

    private final int code;

    private HeroAction(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
