package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.InfoRequest;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class InfoRequestBuilder implements AbstractRequestBuilder<InfoRequest> {

    private long staleTime = -1;

    public InfoRequestBuilder setStaleTime(final long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public InfoRequest build(String clientId) {
        final InfoRequest request = new InfoRequest(clientId);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
