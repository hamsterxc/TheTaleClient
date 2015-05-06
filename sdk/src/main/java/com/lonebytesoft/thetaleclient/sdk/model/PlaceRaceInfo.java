package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceRaceInfo {

    public final Race race;
    public final double value;
    public final double delta;
    public final double influence;

    public PlaceRaceInfo(final JSONObject json) throws JSONException {
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        value = json.getDouble("percents");
        delta = json.getDouble("delta");
        influence = json.getDouble("persons");
    }

}
