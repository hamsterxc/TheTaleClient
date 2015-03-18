package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.02.2015
 */
public enum CompanionType {

    ALIVE("живой"),
    MECH("магомеханический"),
    UNCOMMON("особый"),
    ;

    private final String name;

    CompanionType(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
