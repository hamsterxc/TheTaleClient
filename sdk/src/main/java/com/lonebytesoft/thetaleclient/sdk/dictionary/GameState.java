package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public enum GameState {

    STOPPED(0, "остановлена"),
    WORKING(1, "запущена"),
    ;

    public final int code;
    public final String name;

    GameState(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
