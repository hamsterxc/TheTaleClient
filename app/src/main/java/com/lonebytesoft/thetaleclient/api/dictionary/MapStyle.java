package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 13.10.2014
 */
public enum MapStyle {

    STANDARD("/game/images/map.png", "Обычная"),
    ALTERNATIVE("/game/images/map_alternative.png", "Альтернативная"),
    WINTER("/game/images/map_winter.png", "Зимняя"),
    LARGE_PIXEL("/game/images/map_large_pixel.png", "Крупный пиксель"),
    ;

    private final String path;
    private final String name;

    private MapStyle(final String path, final String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

}
