package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.TakeCardResponse;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 10.10.2014
 */
public class TakeCardRequest extends AbstractApiRequest<TakeCardResponse> {

    public TakeCardRequest() {
        super(HttpMethod.POST, "game/cards/api/get", "1.0", true);
    }

    public void execute(final ApiResponseCallback<TakeCardResponse> callback) {
        execute(null, null, callback);
    }

    protected TakeCardResponse getResponse(final String response) throws JSONException {
        return new TakeCardResponse(response);
    }

    @Override
    protected void retry(final Map<String, String> getParams, final Map<String, String> postParams,
                         final TakeCardResponse response, final ApiResponseCallback<TakeCardResponse> callback) {
        new PostponedTaskRequest(response.statusUrl).execute(new ApiResponseCallback<CommonResponse>() {
            @Override
            public void processResponse(CommonResponse response) {
                TakeCardResponse takeCardResponse;
                try {
                    takeCardResponse = new TakeCardResponse(response.rawResponse);
                    callback.processResponse(takeCardResponse);
                } catch (JSONException e) {
                    try {
                        takeCardResponse = new TakeCardResponse(RequestUtils.getGenericErrorResponse(e.getMessage()));
                    } catch (JSONException ignored) {
                        takeCardResponse = null;
                    }
                    callback.processError(takeCardResponse);
                }
            }

            @Override
            public void processError(CommonResponse response) {
                TakeCardResponse takeCardResponse;
                try {
                    takeCardResponse = new TakeCardResponse(response.rawResponse);
                } catch (JSONException e) {
                    try {
                        takeCardResponse = new TakeCardResponse(RequestUtils.getGenericErrorResponse(e.getMessage()));
                    } catch (JSONException ignored) {
                        takeCardResponse = null;
                    }
                }
                callback.processError(takeCardResponse);
            }
        });
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
