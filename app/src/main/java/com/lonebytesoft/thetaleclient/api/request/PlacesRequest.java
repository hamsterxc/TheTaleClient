package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.PlacesResponse;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 04.05.2015
 */
public class PlacesRequest extends AbstractApiRequest<PlacesResponse> {

    public PlacesRequest() {
        super(HttpMethod.GET, "game/map/places/api/list", "1.0", true);
    }

    public void execute(final ApiResponseCallback<PlacesResponse> callback) {
        execute(null, null, callback);
    }

    @Override
    protected PlacesResponse getResponse(String response) throws JSONException {
        return new PlacesResponse(response);
    }

    @Override
    protected long getStaleTime() {
        return 2 * 60 * 60 * 1000; // 2 hours
    }

}
