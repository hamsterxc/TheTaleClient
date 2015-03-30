package com.lonebytesoft.thetaleclient.sdk;

import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.request.PostponedTaskRequest;
import com.lonebytesoft.thetaleclient.sdk.response.CommonResponse;

/**
 * @author Hamster
 * @since 19.03.2015
 */
public abstract class AbstractAsyncRequest<T extends AbstractApiResponse> extends AbstractRequest<T> {

    private static final long REQUEST_PAUSE_MILLIS = 500; // 500 ms

    @Override
    protected String executeRequest() throws ApiException {
        String responseData = super.executeRequest();
        CommonResponse response = new CommonResponse(responseData);
        while(response.status == ApiResponseStatus.PROCESSING) {
            try {
                Thread.sleep(REQUEST_PAUSE_MILLIS);
            } catch (InterruptedException ignored) {
            }

            responseData = new PostponedTaskRequest(response.statusUrl).execute();
            response = new CommonResponse(responseData);
        }

        return responseData;
    }

}
