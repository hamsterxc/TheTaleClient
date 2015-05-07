package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 30.04.2015
 */
public class MapCellRequest extends AbstractApiGetRequest<MapCellResponse> {

    private static final long STALE_TIME = 60 * 60 * 1000; // 1 hour

    private final String url;

    public MapCellRequest(final int x, final int y) {
        final Map<String, String> getParams = new HashMap<>();
        getParams.put("x", String.valueOf(x));
        getParams.put("y", String.valueOf(y));
        url = RequestUtils.appendGetParams("http://the-tale.org/game/map/cell-info", getParams);

        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public MapCellResponse execute() throws ApiException, JSONException {
        return new MapCellResponse(executeRequest());
    }

}
