package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellWindSpeed {

    // codes must be lowercase (for parsing)
    CALM("штиль"),
    LIGHT_AIR("тихий ветер"),
    LIGHT_BREEZE("лёгкий ветер"),
    GENTLE_BREEZE("слабый ветер"),
    MODERATE_BREEZE("умеренный ветер"),
    FRESH_BREEZE("свежий ветер"),
    STRONG_BREEZE("сильный ветер"),
    MODERATE_GALE("крепкий ветер"),
    ;

    private final String code;

    private MapCellWindSpeed(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
