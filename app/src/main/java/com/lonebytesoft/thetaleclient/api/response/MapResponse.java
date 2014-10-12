package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.model.MapPlaceInfo;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public class MapResponse {

    public final String formatVersion;
    public final String mapVersion;
    public final Map<Integer, MapPlaceInfo> places;
    // todo roads
    public final int width;
    public final int height;
    // todo draw info

    public MapResponse(final String response) throws JSONException {
        final JSONObject json = new JSONObject(response);

        formatVersion = json.getString("format_version");

        final JSONObject placesJson = json.getJSONObject("places");
        places = new HashMap<>(placesJson.length());
        for(final Iterator<String> placesIterator = placesJson.keys(); placesIterator.hasNext();) {
            final String key = placesIterator.next();
            places.put(Integer.decode(key), ObjectUtils.getModelFromJson(MapPlaceInfo.class, placesJson.getJSONObject(key)));
        }

        width = json.getInt("width");
        height = json.getInt("height");
        mapVersion = json.getString("map_version");
    }

}
