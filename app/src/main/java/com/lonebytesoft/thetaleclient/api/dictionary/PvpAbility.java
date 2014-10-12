package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum PvpAbility {

    ICE("ice"),
    BLOOD("blood"),
    FLAME("flame"),
    ;

    private final String code;

    private PvpAbility(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
