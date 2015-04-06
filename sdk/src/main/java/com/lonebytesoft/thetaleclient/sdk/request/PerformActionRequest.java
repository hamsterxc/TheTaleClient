package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractAsyncRequest;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Action;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.CommonResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 06.04.2015
 */
public class PerformActionRequest extends AbstractAsyncRequest<CommonResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/abilities/%s/api/use";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public PerformActionRequest(final String clientId, final Action action) {
        url = RequestUtils.getApiUrl(String.format(METHOD, action.code));
        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        postParams = new HashMap<>();
    }

    public PerformActionRequest(final String clientId, final Action action, final int entityId) {
        this(clientId, action);

        switch(action) {
            case ARENA_ACCEPT:
                getParams.put("battle", String.valueOf(entityId));
                break;

            case BUILDING_REPAIR:
                getParams.put("building", String.valueOf(entityId));
                break;
        }
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(String csrfToken) {
        postParams.put(RequestUtils.PARAM_POST_CSRF_TOKEN, csrfToken);
        return RequestUtils.getHttpPostRequestWithParams(url, getParams, postParams);
    }

    @Override
    public CommonResponse execute() throws ApiException, JSONException {
        return new CommonResponse(executeRequest());
    }

}
