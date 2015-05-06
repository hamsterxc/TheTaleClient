package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public enum PlaceParameter {

    SIZE("size", "размер"),
    ECONOMIC("economic", "уровень экономики"),
    POLITIC_RADIUS("politic_radius", "радиус владений"),
    TERRAIN_RADIUS("terrain_radius", "радиус изменений"),
    STABILITY("stability", "стабильность"),
    PRODUCTION("production", "производство"),
    GOODS("goods", "текущие товары"),
    KEEPERS_GOODS("keepers_goods", "дары Хранителей"),
    SAFETY("safety", "безопасность"),
    TRANSPORT("transport", "транспорт"),
    FREEDOM("freedom", "свобода"),
    TAX("tax", "пошлина"),
    ;

    public final String code;
    public final String name;

    PlaceParameter(final String code, final String name) {
        this.code = code;
        this.name = name;
    }

}
