package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.02.2015
 */
public enum CompanionFeatureType {
    
    ROAD("дорожная", "влияет на скорость путешествия героя"),
    BATTLE("боевая", "влияет на битвы"),
    MONEY("денежная", "влияет на деньги и предметы"),
    UNCOMMON("необычная", "имеет особый эффект"),
    PERSISTENT("неизменная", "оказывает постоянный эффект, независимо от других свойств спутника или героя"),
    ;

    private final String name;
    private final String description;

    private CompanionFeatureType(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
