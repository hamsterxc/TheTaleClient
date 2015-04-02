package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum CompanionFeatureType {
    
    ROAD("дорожная", "влияет на скорость путешествия героя"),
    BATTLE("боевая", "влияет на битвы"),
    MONEY("денежная", "влияет на деньги и предметы"),
    UNCOMMON("необычная", "имеет особый эффект"),
    PERSISTENT("неизменная", "оказывает постоянный эффект, независимо от других свойств спутника или героя"),
    ;

    public final String code;
    public final String name;
    public final String description;

    CompanionFeatureType(final String name, final String description) {
        this.code = name;
        this.name = name;
        this.description = description;
    }

}
