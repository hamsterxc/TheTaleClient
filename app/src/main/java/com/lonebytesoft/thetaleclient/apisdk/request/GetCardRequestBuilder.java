package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.GetCardRequest;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class GetCardRequestBuilder implements AbstractRequestBuilder<GetCardRequest> {

    @Override
    public GetCardRequest build(String clientId) {
        return new GetCardRequest(clientId);
    }

}
