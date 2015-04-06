package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractAsyncRequest;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardTargetType;
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
public class UseCardRequest extends AbstractAsyncRequest<CommonResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/cards/api/use";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public UseCardRequest(final String clientId, final int cardId) {
        url = RequestUtils.getApiUrl(METHOD);

        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        getParams.put("card", String.valueOf(cardId));

        postParams = new HashMap<>();
    }

    public UseCardRequest(final String clientId, final int cardId,
                          final CardTargetType targetType, final int entityId) {
        this(clientId, cardId);

        switch(targetType) {
            case PERSON:
                postParams.put("person", String.valueOf(entityId));
                break;

            case PLACE:
                postParams.put("place", String.valueOf(entityId));
                break;

            case BUILDING:
                postParams.put("building", String.valueOf(entityId));
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
