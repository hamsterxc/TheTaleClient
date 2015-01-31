package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 25.01.2015
 */
public enum ArtifactOrientation {

    FULL_MAGIC("магическая"),
    MAGIC("ближе к магии"),
    EQUAL("равновесие"),
    PHYSICAL("ближе к физике"),
    FULL_PHYSICAL("физическая"),
    ;

    private final String code;

    private ArtifactOrientation(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
