package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum Race {

    HUMAN(0, "человек", "люди"),
    ELF(1, "эльф", "эльфы"),
    ORC(2, "орк", "орки"),
    GOBLIN(3, "гоблин", "гоблины"),
    DWARF(4, "дварф", "дварфы"),
    ;

    private final int code;
    private final String name;
    private final String namePlural;

    private Race(final int code, final String name, final String namePlural) {
        this.code = code;
        this.name = name;
        this.namePlural = namePlural;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNamePlural() {
        return namePlural;
    }

}
