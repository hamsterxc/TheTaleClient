package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceSpecializationModifier {

    public final String name;
    public final double value;

    public PlaceSpecializationModifier(final JSONObject json) throws JSONException {
        name = json.getString("name");
        value = json.getDouble("value");
    }

}
