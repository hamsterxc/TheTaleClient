package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellTemperature {

    // codes must be lowercase (for parsing)
    EXTREMELY_COLD("ужасно холодно"), // theoretically
    VERY_COLD("очень холодно"),
    COLD("холодно"),
    COOL("прохладно"),
    AVERAGE("умеренная температура"),
    WARM("тепло"),
    HOT("жарко"),
    VERY_HOT("очень жарко"),
    EXTREMELY_HOT("ужасно жарко"),
    ;

    private final String code;

    private MapCellTemperature(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
