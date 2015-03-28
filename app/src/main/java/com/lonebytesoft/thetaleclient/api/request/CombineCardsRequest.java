package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.CombineCardsResponse;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 21.03.2015
 */
public class CombineCardsRequest extends AbstractApiRequest<CombineCardsResponse> {

    private final List<Integer> cardIds;

    public CombineCardsRequest(final List<Integer> cardIds) {
        super(HttpMethod.POST, "game/cards/api/combine", "1.0", true);
        this.cardIds = cardIds;
    }

    public void execute(final ApiResponseCallback<CombineCardsResponse> callback) {
        final StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for(final int cardId : cardIds) {
            if(first) {
                first = false;
            } else {
                stringBuilder.append(",");
            }
            stringBuilder.append(cardId);
        }

        final Map<String, String> getParams = new HashMap<>(1);
        getParams.put("cards", stringBuilder.toString());
        execute(getParams, null, callback);
    }

    protected CombineCardsResponse getResponse(final String response) throws JSONException {
        return new CombineCardsResponse(response);
    }

    @Override
    protected void retry(final Map<String, String> getParams, final Map<String, String> postParams,
                         final CombineCardsResponse response, final ApiResponseCallback<CombineCardsResponse> callback) {
        new PostponedTaskRequest(response.statusUrl).execute(new ApiResponseCallback<CommonResponse>() {
            @Override
            public void processResponse(CommonResponse response) {
                CombineCardsResponse combineCardsResponse;
                try {
                    combineCardsResponse = new CombineCardsResponse(response.rawResponse);
                    callback.processResponse(combineCardsResponse);
                } catch (JSONException e) {
                    try {
                        combineCardsResponse = new CombineCardsResponse(RequestUtils.getGenericErrorResponse(e.getMessage()));
                    } catch (JSONException ignored) {
                        combineCardsResponse = null;
                    }
                    callback.processError(combineCardsResponse);
                }
            }

            @Override
            public void processError(CommonResponse response) {
                CombineCardsResponse combineCardsResponse;
                try {
                    combineCardsResponse = new CombineCardsResponse(response.rawResponse);
                } catch (JSONException e) {
                    try {
                        combineCardsResponse = new CombineCardsResponse(RequestUtils.getGenericErrorResponse(e.getMessage()));
                    } catch (JSONException ignored) {
                        combineCardsResponse = null;
                    }
                }
                callback.processError(combineCardsResponse);
            }
        });
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
