package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 03.10.2014
 */
public class AbilityUseRequest extends AbstractApiRequest<CommonResponse> {

    private final Action action;

    public AbilityUseRequest(final Action action) {
        super(HttpMethod.POST, String.format("game/abilities/%s/api/use", action.getCode()), "1.0", true);
        this.action = action;
    }

    public void execute(final int targetId, final ApiResponseCallback<CommonResponse> callback) {
        final Map<String, String> getParams = new HashMap<>(1);
        switch(action) {
            case ARENA_ACCEPT:
                getParams.put("battle", String.valueOf(targetId));
                break;

            case BUILDING_REPAIR:
                getParams.put("building", String.valueOf(targetId));
                break;
        }
        execute(getParams, null, callback);
    }

    @Override
    protected CommonResponse getResponse(final String response) throws JSONException {
        return new CommonResponse(response);
    }

    @Override
    protected void retry(final Map<String, String> getParams, final Map<String, String> postParams,
                         final CommonResponse response, final ApiResponseCallback<CommonResponse> callback) {
        new PostponedTaskRequest(response.statusUrl).execute(callback);
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
