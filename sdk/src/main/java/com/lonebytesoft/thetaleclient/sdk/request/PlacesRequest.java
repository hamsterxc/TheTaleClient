package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.PlacesResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class PlacesRequest extends AbstractApiGetRequest<PlacesResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/map/places/api/list";
    private static final long STALE_TIME = 60 * 60 * 1000; // 1 hour

    private final String url;

    public PlacesRequest(final String clientId) {
        final Map<String, String> getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        url = RequestUtils.appendGetParams(RequestUtils.getApiUrl(METHOD), getParams);
        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public PlacesResponse execute() throws ApiException, JSONException {
        return new PlacesResponse(executeRequest());
    }

}
