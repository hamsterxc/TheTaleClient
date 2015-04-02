package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public enum EquipmentType {

    MAIN_HAND(0, "основная рука"),
    OFF_HAND(1, "вспомогательная рука"),
    HEAD(2, "шлем"),
    AMULET(9, "амулет"),
    SHOULDERS(3, "наплечники"),
    BODY(4, "доспех"),
    GLOVES(5, "перчатки"),
    CLOAK(6, "плащ"),
    TROUSERS(7, "штаны"),
    BOOTS(8, "сапоги"),
    RING(10, "кольцо"),
    ;

    public final int code;
    public final String name;

    EquipmentType(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
