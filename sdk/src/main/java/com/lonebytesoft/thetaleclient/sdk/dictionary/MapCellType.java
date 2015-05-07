package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 30.04.2015
 */
public enum MapCellType {

    PLACE("Город"),
    BUILDING("Строение"),
    TERRAIN("Местность"),
    ;

    public final String code;
    public final String name;

    MapCellType(final String name) {
        this.code = name;
        this.name = name;
    }

}
