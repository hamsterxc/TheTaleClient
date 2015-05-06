package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceClanInfo {

    public final int id;
    public final String name;
    public final String nameShort;

    public PlaceClanInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        nameShort = json.getString("abbr");
    }

}
