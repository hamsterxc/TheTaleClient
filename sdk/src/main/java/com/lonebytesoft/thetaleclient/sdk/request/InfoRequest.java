package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class InfoRequest extends AbstractApiGetRequest<InfoResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "api/info";
    private static final long STALE_TIME = 10000; // 10 seconds

    private final String url;

    public InfoRequest(final String clientId) {
        final Map<String, String> getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        url = RequestUtils.appendGetParams(RequestUtils.getApiUrl(METHOD), getParams);
        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public InfoResponse execute() throws ApiException, JSONException {
        return new InfoResponse(executeRequest());
    }

}
