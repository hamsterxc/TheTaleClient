package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
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

    private final int code;
    private final String name;

    private ArtifactType(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
