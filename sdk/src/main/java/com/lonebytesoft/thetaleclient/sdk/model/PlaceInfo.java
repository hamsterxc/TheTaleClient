package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.PlaceSpecialization;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 05.05.2015
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
        final Integer specializationCode = ObjectUtils.getOptionalInteger(json, "specialization");
        specialization = specializationCode == null
                ? null
                : ObjectUtils.getEnumForCode(PlaceSpecialization.class, specializationCode);
        isFrontier = json.getBoolean("frontier");
    }

}
