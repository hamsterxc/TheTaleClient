package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum ArtifactRarity {

    COMMON(0, "обычный артефакт"),
    RARE(1, "редкий артефакт"),
    EPIC(2, "эпический артефакт"),
    ;

    public final int code;
    public final String name;

    ArtifactRarity(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
