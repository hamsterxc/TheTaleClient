package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum Habit {

    PEACEFULNESS("peacefulness", "миролюбие"),
    HONOR("honor", "честь"),
    ;

    public final String code;
    public final String name;

    Habit(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

}
