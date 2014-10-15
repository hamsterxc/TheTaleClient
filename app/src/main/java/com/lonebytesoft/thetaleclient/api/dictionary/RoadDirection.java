package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 13.10.2014
 */
public enum RoadDirection {

    LEFT("l"),
    UP("u"),
    RIGHT("r"),
    DOWN("d"),
    ;

    private final String code;

    private RoadDirection(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
