package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum ArtifactType {

    JUNK(0, "хлам"),
    MAIN_HAND(1, "основная рука"),
    OFF_HAND(2, "вторая рука"),
    BODY(3, "доспех"),
    AMULET(4, "амулет"),
    HEAD(5, "шлем"),
    CLOAK(6, "плащ"),
    SHOULDERS(7, "наплечники"),
    GLOVES(8, "перчатки"),
    TROUSERS(9, "штаны"),
    BOOTS(10, "обувь"),
    RING(11, "кольцо"),
    ;

    public final int code;
    public final String name;

    ArtifactType(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
