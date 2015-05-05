package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.PlaceResponse;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class PlaceRequest extends AbstractApiRequest<PlaceResponse> {

    public PlaceRequest(final int placeId) {
        super(HttpMethod.GET, String.format("game/map/places/%d/api/show", placeId), "1.0", true);
    }

    public void execute(final ApiResponseCallback<PlaceResponse> callback) {
        execute(null, null, callback);
    }

    @Override
    protected PlaceResponse getResponse(String response) throws JSONException {
        return new PlaceResponse(response);
    }

    @Override
    protected long getStaleTime() {
        return 2 * 60 * 60 * 1000; // 2 hours
    }

}
