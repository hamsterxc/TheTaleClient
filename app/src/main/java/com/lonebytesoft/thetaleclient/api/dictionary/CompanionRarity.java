package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.02.2015
 */
public enum CompanionRarity {

    COMMON("обычный спутник"),
    UNCOMMON("необычный спутник"),
    RARE("редкий спутник"),
    EPIC("эпический спутник"),
    LEGENDARY("легендарный спутник"),
    ;

    private final String name;

    private CompanionRarity(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
