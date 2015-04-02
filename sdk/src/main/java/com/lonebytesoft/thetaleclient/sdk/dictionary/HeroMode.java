package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public enum HeroMode {

    PVE("pve", "PvE"),
    PVP("pvp", "PvP"),
    ;

    public final String code;
    public final String name;

    HeroMode(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

}
