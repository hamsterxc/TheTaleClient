package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellHumidity {

    // codes must be lowercase (for parsing)
    EXTREMELY_DRY("ужасно сухо"),
    VERY_DRY("очень сухо"),
    DRY("сухо"),
    DRYISH("пониженная влажность"),
    AVERAGE("умеренная влажность"),
    WETTISH("повышенная влажность"),
    WET("влажно"),
    VERY_WET("очень влажно"),
    EXTREMELY_WET("ужасно влажно"), // theoretically
    ;

    private final String code;

    private MapCellHumidity(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
