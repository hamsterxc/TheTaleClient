package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum Gender {

    MALE(0, "мужчина"),
    FEMALE(1, "женщина"),
    IT(2, "оно"),
    ;

    public final int code;
    public final String name;

    Gender(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
