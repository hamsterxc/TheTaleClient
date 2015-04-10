package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.LogoutRequest;

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
