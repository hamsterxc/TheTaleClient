package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.GetCardRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

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
