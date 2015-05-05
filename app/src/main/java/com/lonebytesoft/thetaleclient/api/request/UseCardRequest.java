package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.dictionary.CardTargetType;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class UseCardRequest extends AbstractApiRequest<CommonResponse> {

    public UseCardRequest() {
        super(HttpMethod.POST, "game/cards/api/use", "1.0", true);
    }

    public void execute(final int cardId, final ApiResponseCallback<CommonResponse> callback) {
        final Map<String, String> getParams = new HashMap<>(1);
        getParams.put("card", String.valueOf(cardId));
        execute(getParams, null, callback);
    }

    public void execute(final int cardId, final CardTargetType targetType, final int targetId,
                        final ApiResponseCallback<CommonResponse> callback) {
        final Map<String, String> getParams = new HashMap<>(1);
        getParams.put("card", String.valueOf(cardId));

        final Map<String, String> postParams = new HashMap<>();
        switch(targetType) {
            case PERSON:
                postParams.put("person", String.valueOf(targetId));
                break;

            case PLACE:
                postParams.put("place", String.valueOf(targetId));
                break;

            case BUILDING:
                postParams.put("building", String.valueOf(targetId));
                break;
        }

        execute(getParams, postParams, callback);
    }

    @Override
    protected CommonResponse getResponse(String response) throws JSONException {
        return null;
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
