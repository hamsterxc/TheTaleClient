package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class HabitInfo {

    public final double value;
    public final String description;

    public HabitInfo(final JSONObject json) throws JSONException {
        value = json.getDouble("raw");
        description = json.getString("verbose");
    }

}
