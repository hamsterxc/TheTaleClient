package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum Race {

    HUMAN(0, "человек"),
    ELF(1, "эльф"),
    ORC(2, "орк"),
    GOBLIN(3, "гоблин"),
    DWARF(4, "дварф"),
    ;

    private final int code;
    private final String name;

    private Race(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
