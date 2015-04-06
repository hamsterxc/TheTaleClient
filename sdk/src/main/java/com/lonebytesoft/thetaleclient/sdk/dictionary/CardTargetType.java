package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 06.04.2015
 */
public enum CardTargetType {

    NONE("герой"),
    PERSON("советник"),
    PLACE("город"),
    BUILDING("здание"),
    ;

    public final String code;
    public final String name;

    CardTargetType(final String name) {
        this.code = name;
        this.name = name;
    }

}
