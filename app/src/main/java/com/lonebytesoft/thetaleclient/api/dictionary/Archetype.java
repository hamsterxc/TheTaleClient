package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 25.01.2015
 */
public enum Archetype {

    ADVENTURER("авантюрист"),
    WARRIOR("воин"),
    MAGE("маг"),
    ;

    private final String code;

    private Archetype(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
