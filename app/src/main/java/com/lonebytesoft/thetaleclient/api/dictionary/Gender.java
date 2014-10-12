package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum Gender {

    MALE(0, "мужчина"),
    FEMALE(1, "женщина"),
    IT(2, "оно"),
    ;

    private final int code;
    private final String name;

    private Gender(final int code, final String name) {
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
