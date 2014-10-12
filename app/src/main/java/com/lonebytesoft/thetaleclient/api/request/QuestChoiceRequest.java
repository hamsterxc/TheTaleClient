package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 05.10.2014
 */
public class QuestChoiceRequest extends AbstractApiRequest<CommonResponse> {

    public QuestChoiceRequest() {
        super(HttpMethod.POST, "game/quests/api/choose", "1.0", true);
    }

    public void execute(final String choiceId, final ApiResponseCallback<CommonResponse> callback) {
        final Map<String, String> getParams = new HashMap<>(1);
        getParams.put("option_uid", choiceId);
        super.execute(getParams, null, callback);
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
