package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class PlacesResponse extends AbstractApiResponse {

    public Map<Integer, PlaceInfo> places;

    public PlacesResponse(String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(JSONObject data) throws JSONException {
        final JSONObject placesJson = data.getJSONObject("places");
        places = new HashMap<>(placesJson.length());
        for(final Iterator<String> placesJsonIterator = placesJson.keys(); placesJsonIterator.hasNext();) {
            final String key = placesJsonIterator.next();
            places.put(Integer.decode(key),
                    ObjectUtils.getModelFromJson(PlaceInfo.class, placesJson.getJSONObject(key)));
        }
    }

}
