package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum CompanionDedication {

    HESITANT("нерешительный"),
    BOLD("смелый"),
    BRAVE("храбрый"),
    VALIANT("доблестный"),
    HEROIC("героический"),
    ;

    public final String code;
    public final String name;

    CompanionDedication(final String name) {
        this.code = name;
        this.name = name;
    }

}
