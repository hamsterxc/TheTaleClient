package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum Race {

    HUMAN(0, "человек"),
    ELF(1, "эльф"),
    ORC(2, "орк"),
    GOBLIN(3, "гоблин"),
    DWARF(4, "дварф"),
    ;

    public final int code;
    public final String name;

    Race(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
