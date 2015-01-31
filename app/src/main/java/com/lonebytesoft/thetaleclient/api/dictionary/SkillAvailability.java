package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 26.01.2015
 */
public enum SkillAvailability {

    PLAYERS("только для игроков"),
    MONSTERS("только для монстров"),
    ALL("для всех"),
    ;

    private final String code;

    private SkillAvailability(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
