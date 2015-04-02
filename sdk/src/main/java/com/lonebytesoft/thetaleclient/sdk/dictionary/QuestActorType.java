package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum QuestActorType {

    PERSON(0, "Горожанин"),
    PLACE(1, "Город"),
    SPENDING(2, "Цель накопления"),
    ;

    public final int code;
    public final String name;

    QuestActorType(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
