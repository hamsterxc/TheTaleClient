package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.02.2015
 */
public enum CompanionDedication {

    HESITANT("нерешительный"),
    BOLD("смелый"),
    BRAVE("храбрый"),
    VALIANT("доблестный"),
    HEROIC("героический"),
    ;

    private final String name;

    private CompanionDedication(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
