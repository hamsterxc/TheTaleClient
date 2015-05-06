package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.PlaceResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceRequest extends AbstractApiGetRequest<PlaceResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/map/places/%d/api/show";
    private static final long STALE_TIME = 60 * 60 * 1000; // 1 hour

    private final String url;

    public PlaceRequest(final String clientId, final int placeId) {
        final Map<String, String> getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        url = RequestUtils.appendGetParams(RequestUtils.getApiUrl(String.format(METHOD, placeId)), getParams);
        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public PlaceResponse execute() throws ApiException, JSONException {
        return new PlaceResponse(executeRequest());
    }

}
