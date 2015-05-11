package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.LogoutRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class LogoutRequestBuilder implements AbstractRequestBuilder<LogoutRequest> {

    @Override
    public LogoutRequest build(String clientId) {
        return new LogoutRequest(clientId);
    }

}
