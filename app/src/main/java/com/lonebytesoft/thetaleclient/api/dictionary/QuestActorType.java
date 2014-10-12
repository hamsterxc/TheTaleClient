package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum QuestActorType {

    PERSON(0, "Горожанин"),
    PLACE(1, "Город"),
    SPENDING(2, "Цель накопления"),
    ;

    private final int code;
    private final String name;

    private QuestActorType(final int code, final String name) {
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
