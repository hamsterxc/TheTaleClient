package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public enum MapStyle {

    STANDARD("Обычная", "game/images/map.png"),
    ALTERNATIVE("Альтернативная", "game/images/map_alternative.png"),
    WINTER("Зимняя", "game/images/map_winter.png"),
    LARGE_PIXEL("Крупный пиксель", "game/images/map_large_pixel.png"),
    ;

    public final String code;
    public final String name;
    public final String path;

    MapStyle(final String name, final String path) {
        this.code = name;
        this.name = name;
        this.path = path;
    }

}
