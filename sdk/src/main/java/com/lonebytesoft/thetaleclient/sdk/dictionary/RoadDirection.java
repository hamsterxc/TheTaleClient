package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public enum RoadDirection {

    LEFT("l"),
    UP("u"),
    RIGHT("r"),
    DOWN("d"),
    ;

    public final String code;
    public final String name;

    RoadDirection(final String code) {
        this.code = code;
        this.name = code;
    }

}
