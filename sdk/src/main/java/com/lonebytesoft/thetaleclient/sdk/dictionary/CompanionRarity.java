package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum CompanionRarity {

    COMMON("обычный спутник"),
    UNCOMMON("необычный спутник"),
    RARE("редкий спутник"),
    EPIC("эпический спутник"),
    LEGENDARY("легендарный спутник"),
    ;

    public final String code;
    public final String name;

    CompanionRarity(final String name) {
        this.code = name;
        this.name = name;
    }

}
