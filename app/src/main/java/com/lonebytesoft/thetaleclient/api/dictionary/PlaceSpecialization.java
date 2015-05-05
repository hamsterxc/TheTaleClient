package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 04.05.2015
 */
public enum PlaceSpecialization {

    NONE(-1, null),
    TRADE_CENTER(0, "Торговый центр"),
    MASTERS_CITY(1, "Город мастеров"),
    FORT(2, "Форт"),
    POLITICS_CENTER(3, "Политический центр"),
    POLIS(4, "Полис"),
    RESORT(5, "Курорт"),
    TRANSPORT_NODE(6, "Транспортный узел"),
    LIBERTY_CITY(7, "Вольница"),
    HOLY_CITY(8, "Святой город"),
    ;

    public final int code;
    public final String name;

    PlaceSpecialization(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

}
