package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractAsyncRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.CombineCardsResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 06.04.2015
 */
public class CombineCardsRequest extends AbstractAsyncRequest<CombineCardsResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "game/cards/api/combine";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public CombineCardsRequest(final String clientId, final List<Integer> cardIds) {
        url = RequestUtils.getApiUrl(METHOD);

        final StringBuilder cardIdsString = new StringBuilder();
        boolean first = true;
        for(final int cardId : cardIds) {
            if(first) {
                first = false;
            } else {
                cardIdsString.append(",");
            }
            cardIdsString.append(cardId);
        }
        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        getParams.put("cards", cardIdsString.toString());

        postParams = new HashMap<>();
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(String csrfToken) {
        postParams.put(RequestUtils.PARAM_POST_CSRF_TOKEN, csrfToken);
        return RequestUtils.getHttpPostRequestWithParams(url, getParams, postParams);
    }

    @Override
    public CombineCardsResponse execute() throws ApiException, JSONException {
        return new CombineCardsResponse(executeRequest());
    }

}
