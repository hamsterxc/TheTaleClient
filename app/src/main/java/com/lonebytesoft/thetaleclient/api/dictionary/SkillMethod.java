package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 26.01.2015
 */
public enum SkillMethod {

    ACTIVE("активная"),
    PASSIVE("пассивная"),
    ;

    private final String code;

    private SkillMethod(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
