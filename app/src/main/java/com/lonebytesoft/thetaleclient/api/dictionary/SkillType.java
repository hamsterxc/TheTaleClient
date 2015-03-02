package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 26.01.2015
 */
public enum SkillType {

    COMBAT("боевая"),
    PEACE("мирная"),
    COMPANION("для спутника"),
    ;

    private final String code;

    private SkillType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
