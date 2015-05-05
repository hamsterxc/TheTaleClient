package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum HeroAction {

    IDLE(0, "герой бездельничает"),
    QUEST(1, "герой выполненяет задание"),
    TRAVEL(2, "герой путешествует между городами"),
    BATTLE(3, "герой сражается 1x1 с монстром"),
    RESURRECTION(4, "герой воскресает"),
    TOWN(5, "герой в городе"),
    REST(6, "герой лечится"),
    EQUIP(7, "герой экипируется"),
    TRADE(8, "герой торгует"),
    NEAR_TOWN(9, "герой путешествует около города"),
    RELIGIOUS(10, "герой восстановливает энергию Хранителю"),
    NO_EFFECT(11, "техническое действие для особых действий героя в заданиях"),
    PROXY_HEROES(12, "техническое прокси-действие для взаимодействия героев"),
    PVP(13, "герой сражается 1x1 с другим героем"),
    TEST(14, "техническое действие для тестов"),
    COMPANION_CARE(15, "герой ухаживает за спутником"),
    ;

    public final int code;
    public final String name;

    HeroAction(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
