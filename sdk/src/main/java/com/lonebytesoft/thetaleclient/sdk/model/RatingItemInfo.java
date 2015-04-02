package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 23.03.2015
 */
public class RatingItemInfo {

    public final String name;
    public final int place;
    public final double value;

    public RatingItemInfo(final JSONObject json) throws JSONException {
        name = json.getString("name");
        place = json.getInt("place");
        value = json.getDouble("value");
    }

}
