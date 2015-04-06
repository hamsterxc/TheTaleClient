package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.ThirdPartyAuthStateResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 03.04.2015
 */
public class ThirdPartyAuthStateRequest extends AbstractApiGetRequest<ThirdPartyAuthStateResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "accounts/third-party/tokens/api/authorisation-state";
    private static final long STALE_TIME = 0;

    private final String url;

    public ThirdPartyAuthStateRequest(final String clientId) {
        final Map<String, String> getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        url = RequestUtils.appendGetParams(RequestUtils.getApiUrl(METHOD), getParams);
        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public ThirdPartyAuthStateResponse execute() throws ApiException, JSONException {
        return new ThirdPartyAuthStateResponse(executeRequest());
    }

}
