package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class PositionInfo {

    public final double x;
    public final double y;
    public final double sightX;
    public final double sightY;

    public PositionInfo(final JSONObject json) throws JSONException {
        x = json.getDouble("x");
        y = json.getDouble("y");
        sightX = json.getDouble("dx");
        sightY = json.getDouble("dy");
    }

}
