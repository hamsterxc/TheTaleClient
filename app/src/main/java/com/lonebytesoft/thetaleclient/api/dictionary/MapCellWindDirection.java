package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellWindDirection {

    // codes must be lowercase (for parsing)
    N("северный"),
    NNE("северо-северо-восточный"),
    NE("северо-восточный"),
    ENE("востоко-северо-восточный"),
    E("восточный"),
    ESE("востоко-юго-восточный"),
    SE("юго-восточный"),
    SSE("юго-юго-восточный"),
    S("южный"),
    SSW("юго-юго-западный"),
    SW("юго-западный"),
    WSW("западо-юго-западный"),
    W("западный"),
    WNW("западо-северо-западный"),
    NW("северо-западный"),
    NNW("северо-северо-западный"),
    ;

    private final String code;

    private MapCellWindDirection(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
