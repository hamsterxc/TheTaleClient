package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum Habit {

    PEACEFULNESS("peacefulness"),
    HONOR("honor"),
    ;

    private final String code;

    private Habit(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
