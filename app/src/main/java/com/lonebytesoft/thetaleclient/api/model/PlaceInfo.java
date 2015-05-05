package com.lonebytesoft.thetaleclient.api.model;

import com.lonebytesoft.thetaleclient.api.dictionary.PlaceSpecialization;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 04.05.2015
 */
public class PlaceInfo {

    public final int id;
    public final String name;
    public final int x;
    public final int y;
    public final int size;
    public final PlaceSpecialization specialization;
    public final boolean isFrontier;

    public PlaceInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        x = json.getJSONObject("position").getInt("x");
        y = json.getJSONObject("position").getInt("y");
        size = json.getInt("size");
        specialization = ObjectUtils.getEnumForCode(PlaceSpecialization.class,
                ObjectUtils.getOptionalInteger(json, "specialization", -1));
        isFrontier = json.getBoolean("frontier");
    }

}
