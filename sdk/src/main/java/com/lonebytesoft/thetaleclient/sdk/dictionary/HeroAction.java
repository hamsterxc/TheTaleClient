package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum HeroAction {

    IDLE(0, "безделье"),
    QUEST(1, "задание"),
    TRAVEL(2, "путешествие между городами"),
    BATTLE(3, "сражение 1x1 с монстром"),
    RESURRECTION(4, "воскрешение"),
    TOWN(5, "действия в городе"),
    REST(6, "отдых"),
    EQUIP(7, "экипировка"),
    TRADE(8, "торговля"),
    NEAR_TOWN(9, "путешествие около города"),
    RELIGIOUS(10, "восстановление энергии"),
    NO_EFFECT(11, "действие без эффекта на игру"),
    PROXY_HEROES(12, "прокси-действия для взаимодействия героев"),
    PVP(13, "PvP 1x1"),
    TEST(14, "проверочное действие"),
    COMPANION_CARE(15, "уход за спутником"),
    ;

    public final int code;
    public final String name;

    HeroAction(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
