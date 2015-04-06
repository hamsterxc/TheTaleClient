package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum QuestActorType {

    PERSON(0, "житель"),
    PLACE(1, "место"),
    SPENDING(2, "трата денег"),
    ;

    public final int code;
    public final String name;

    QuestActorType(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
