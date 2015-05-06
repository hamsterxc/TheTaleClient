package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public class MapPlaceInfo {

    public final int id;
    public final int size;
    public final Race race;
    public final String name;
    public final int x;
    public final int y;

    public MapPlaceInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        size = json.getInt("size");
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        name = json.getString("name");
        x = json.getJSONObject("pos").getInt("x");
        y = json.getJSONObject("pos").getInt("y");
    }

}
