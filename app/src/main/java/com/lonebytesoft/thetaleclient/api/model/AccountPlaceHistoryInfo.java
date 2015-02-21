package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 20.02.2015
 */
public class AccountPlaceHistoryInfo {

    public final int placeId;
    public final String name;
    public final int helpCount;

    public AccountPlaceHistoryInfo(final JSONObject json) throws JSONException {
        placeId = json.getJSONObject("place").getInt("id");
        name = json.getJSONObject("place").getString("name");
        helpCount = json.getInt("count");
    }

}
