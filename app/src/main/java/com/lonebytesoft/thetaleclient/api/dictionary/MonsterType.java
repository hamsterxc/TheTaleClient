package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 25.01.2015
 */
public enum MonsterType {

    DEMON("демоны"),
    SAVAGE("дикари"),
    ANIMAL("животные"),
    CONSTRUCT("конструкты"),
    INSECT("насекомые"),
    UNDEAD("нежить"),
    PLANT("растения"),
    ELEMENTAL("стихийные существа"),
    REPTILIAN("хладнокровные гады"),
    CIVILIZED("цивилизованные существа"),
    BEAST("чудовища"),
    ;

    private final String code;

    private MonsterType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
