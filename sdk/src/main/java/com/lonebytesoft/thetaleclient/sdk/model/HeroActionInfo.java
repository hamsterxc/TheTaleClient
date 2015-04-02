package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.HeroAction;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class HeroActionInfo {

    public final HeroAction type;
    public final double completion;
    public final String description;
    public final String infoUrl;
    public final boolean isBossFight;

    public HeroActionInfo(final JSONObject json) throws JSONException {
        type = ObjectUtils.getEnumForCode(HeroAction.class, json.getInt("type"));
        completion = json.getDouble("percents");
        description = json.getString("description");
        infoUrl = ObjectUtils.getOptionalString(json, "info_link");
        isBossFight = json.optBoolean("is_boss", false); // TODO undocumented feature
    }

}
