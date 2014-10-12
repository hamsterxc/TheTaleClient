package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public enum GameState {

    STOPPED(0),
    WORKING(1),
    ;

    private final int code;

    private GameState(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
