package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.PlaceSpecialization;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceSpecializationInfo {

    public final PlaceSpecialization specialization;
    public final double value;
    public final double sizeMultiplier;
    public final List<PlaceSpecializationModifier> modifiers;

    public PlaceSpecializationInfo(final JSONObject json) throws JSONException {
        specialization = ObjectUtils.getEnumForCode(PlaceSpecialization.class, json.getInt("value"));
        value = json.getDouble("power");
        sizeMultiplier = json.getDouble("size_modifier");
        modifiers = ObjectUtils.getModelListFromJson(
                PlaceSpecializationModifier.class,
                json.getJSONArray("modifiers"),
                new String[]{"name", "value"});
    }

}
