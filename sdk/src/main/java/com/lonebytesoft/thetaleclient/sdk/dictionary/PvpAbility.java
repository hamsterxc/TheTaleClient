package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public enum PvpAbility {

    ICE("ice", "Лёд"),
    BLOOD("blood", "Кровь"),
    FLAME("flame", "Пламя"),
    ;

    public final String code;
    public final String name;

    PvpAbility(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

}
