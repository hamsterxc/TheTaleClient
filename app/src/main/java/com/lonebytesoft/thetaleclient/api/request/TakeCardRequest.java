package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 10.10.2014
 * todo undocumented request
 */
public class TakeCardRequest extends AbstractApiRequest<CommonResponse> {

    public TakeCardRequest(final int accountId) {
        super(HttpMethod.POST, String.format("game/heroes/%d/get-card", accountId), "", false);
    }

    public void execute(final ApiResponseCallback<CommonResponse> callback) {
        execute(null, null, callback);
    }

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
