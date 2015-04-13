package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;
import com.lonebytesoft.thetaleclient.sdk.Urls;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public class MapRequest extends AbstractApiGetRequest<MapResponse> {

    private final String url;
    private static final long STALE_TIME = 60 * 60 * 1000; // 1 hour

    public MapRequest(final String dynamicContentUrl, final String mapVersion) {
        url = String.format("%s%smap/region-%s.js", Urls.BASE_PROTOCOL, dynamicContentUrl, mapVersion);
        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public MapResponse execute() throws ApiException, JSONException {
        return new MapResponse(executeRequest());
    }

}
