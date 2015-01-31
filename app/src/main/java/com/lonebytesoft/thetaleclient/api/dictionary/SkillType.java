package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 26.01.2015
 */
public enum SkillType {

    COMBAT("боевая"),
    PEACE("небоевая"),
    ;

    private final String code;

    private SkillType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
