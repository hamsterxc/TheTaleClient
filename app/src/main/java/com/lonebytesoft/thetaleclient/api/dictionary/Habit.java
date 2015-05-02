package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum Habit {

    HONOR(0, "honor", "честь"),
    PEACEFULNESS(1, "peacefulness", "миролюбие"),
    ;

    public final int code;
    public final String name;
    public final String description;

    Habit(final int code, final String name, final String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
