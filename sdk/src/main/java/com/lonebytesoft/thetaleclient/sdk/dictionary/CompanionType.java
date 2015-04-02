package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum CompanionType {

    ALIVE("живой"),
    MECH("магомеханический"),
    UNCOMMON("особый"),
    ;

    public final String code;
    public final String name;

    CompanionType(final String name) {
        this.code = name;
        this.name = name;
    }

}
