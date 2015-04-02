package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class HabitInfo {

    public final double value;
    public final String description;

    public HabitInfo(final JSONObject json) throws JSONException {
        value = json.getDouble("raw");
        description = json.getString("verbose");
    }

}
