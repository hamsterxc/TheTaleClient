package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceParameterInfo {

    public final double value;
    public final List<PlaceParameterModifier> modifiers;

    public PlaceParameterInfo(final JSONObject json) throws JSONException {
        value = json.getDouble("value");
        if(json.isNull("modifiers")) {
            modifiers = null;
        } else {
            modifiers = ObjectUtils.getModelListFromJson(
                    PlaceParameterModifier.class,
                    json.getJSONArray("modifiers"),
                    new String[]{"name", "value"});
        }
    }

}
