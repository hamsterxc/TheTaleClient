package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellHumidity {

    EXTREMELY_DRY("ужасно сухо"),
    VERY_DRY("очень сухо"),
    DRY("сухо"),
    DRYISH("пониженная влажность"),
    AVERAGE("умеренная влажность"),
    WETTISH("повышенная влажность"),
    WET("влажно"),
    VERY_WET("очень влажно"),
    FOG("туман"),
    THICK_FOG("сильный туман"),
    ;

    private final String code;

    private MapCellHumidity(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
