package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public enum HeroMode {

    PVE("pve"),
    PVP("pvp"),
    ;

    private final String code;

    private HeroMode(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
