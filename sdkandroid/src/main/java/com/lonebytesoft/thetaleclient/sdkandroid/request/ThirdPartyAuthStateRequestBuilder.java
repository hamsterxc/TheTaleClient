package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.ThirdPartyAuthStateRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class ThirdPartyAuthStateRequestBuilder implements AbstractRequestBuilder<ThirdPartyAuthStateRequest> {

    private long staleTime = -1;

    public ThirdPartyAuthStateRequestBuilder setStaleTime(long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public ThirdPartyAuthStateRequest build(String clientId) {
        final ThirdPartyAuthStateRequest request = new ThirdPartyAuthStateRequest(clientId);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
