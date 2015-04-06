package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractAsyncRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.GetCardResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 19.03.2015
 */
public class GetCardRequest extends AbstractAsyncRequest<GetCardResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/cards/api/get";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public GetCardRequest(final String clientId) {
        url = RequestUtils.getApiUrl(METHOD);
        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        postParams = new HashMap<>();
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(String csrfToken) {
        postParams.put(RequestUtils.PARAM_POST_CSRF_TOKEN, csrfToken);
        return RequestUtils.getHttpPostRequestWithParams(url, getParams, postParams);
    }

    @Override
    public GetCardResponse execute() throws ApiException, JSONException {
        return new GetCardResponse(executeRequest());
    }

}
