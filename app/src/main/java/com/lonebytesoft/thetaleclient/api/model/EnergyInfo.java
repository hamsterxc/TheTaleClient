package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class EnergyInfo {

    public final int current;
    public final int max;
    public final int bonus;
    public final int discount;

    public EnergyInfo(final JSONObject json) throws JSONException {
        current = json.getInt("value");
        max = json.getInt("max");
        bonus = json.getInt("bonus");
        discount = json.getInt("discount");
    }

}
