package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceHabitInfo {

    public final int intervalCode;
    public final double value;
    public final double delta;
    public final double positive;
    public final double negative;

    public PlaceHabitInfo(final JSONObject json) throws JSONException {
        intervalCode = json.getInt("interval");
        value = json.getDouble("value");
        delta = json.getDouble("delta");
        positive = json.getDouble("positive_points");
        negative = json.getDouble("negative_points");
    }

}
