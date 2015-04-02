package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum Archetype {

    ADVENTURER("авантюрист"),
    WARRIOR("воин"),
    MAGE("маг"),
    ;

    public final String code;
    public final String name;

    Archetype(final String name) {
        this.code = name;
        this.name = name;
    }

}
