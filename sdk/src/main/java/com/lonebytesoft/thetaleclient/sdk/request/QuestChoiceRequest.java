package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractAsyncRequest;
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
public class QuestChoiceRequest extends AbstractAsyncRequest<CommonResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/quests/api/choose";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public QuestChoiceRequest(final String clientId, final String choiceId) {
        url = RequestUtils.getApiUrl(METHOD);

        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        getParams.put("option_uid", choiceId);

        postParams = new HashMap<>();
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
