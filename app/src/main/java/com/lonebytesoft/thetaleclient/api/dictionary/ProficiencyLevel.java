package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 25.12.2014
 */
public enum ProficiencyLevel {

    IMPROFICIENT("полная непригодность"),
    TALENTLESS("бездарность"),
    IGNORAMUS("невежда"),
    ILLITERATE("неуч"),
    DISCIPLE("ученик"),
    APPRENTICE("подмастерье"),
    HANDYMAN("умелец"),
    MASTER("мастер"),
    EXPERT("эксперт"),
    MAITRE("мэтр"),
    GENIUS("гений"),
    ;

    private final String code;

    private ProficiencyLevel(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
