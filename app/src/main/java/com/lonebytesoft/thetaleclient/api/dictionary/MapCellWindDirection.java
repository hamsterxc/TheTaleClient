package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 17.10.2014
 */
public enum MapCellWindDirection {

    N("северный", 0),
    NNE("северо-северо-восточный", 22.5),
    NE("северо-восточный", 45),
    ENE("востоко-северо-восточный", 67.5),
    E("восточный", 90),
    ESE("востоко-юго-восточный", 112.5),
    SE("юго-восточный", 135),
    SSE("юго-юго-восточный", 157.5),
    S("южный", 180),
    SSW("юго-юго-западный", 202.5),
    SW("юго-западный", 225),
    WSW("западо-юго-западный", 247.5),
    W("западный", 270),
    WNW("западо-северо-западный", 292.5),
    NW("северо-западный", 315),
    NNW("северо-северо-западный", 337.5),
    ;

    private final String code;
    private final double direction;

    private MapCellWindDirection(final String code, final double direction) {
        this.code = code;
        this.direction = direction;
    }

    public String getCode() {
        return code;
    }

    public double getDirection() {
        return direction;
    }

}
