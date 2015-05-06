package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class BillInfo {

    public final int id;
    public final String name;
    public final List<String> properties;

    public BillInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("caption");

        final JSONArray propertiesJson = json.getJSONArray("properties");
        final int propertiesCount = propertiesJson.length();
        properties = new ArrayList<>(propertiesCount);
        for(int i = 0; i < propertiesCount; i++) {
            properties.add(propertiesJson.getString(i));
        }
    }

}
