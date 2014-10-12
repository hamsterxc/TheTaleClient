package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 03.10.2014
 */
public class PostponedTaskRequest extends AbstractApiRequest<CommonResponse> {

    public PostponedTaskRequest(final String url) {
        super(HttpMethod.GET, url, "", false);
    }

    public void execute(final ApiResponseCallback<CommonResponse> callback) {
        execute(null, null, callback);
    }

    protected CommonResponse getResponse(final String response) throws JSONException {
        return new CommonResponse(response);
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
