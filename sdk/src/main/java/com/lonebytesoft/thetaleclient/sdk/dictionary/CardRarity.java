package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public enum CardRarity {

    COMMON(0, "обычная карта"),
    UNCOMMON(1, "необычная карта"),
    RARE(2, "редкая карта"),
    EPIC(3, "эпическая карта"),
    LEGENDARY(4, "легендарная карта"),
    ;

    public final int code;
    public final String name;

    CardRarity(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
