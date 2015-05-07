package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.PlacesRequest;

/**
 * @author Hamster
 * @since 07.05.2015
 */
public class PlacesRequestBuilder implements AbstractRequestBuilder<PlacesRequest> {

    private long staleTime = -1;

    public PlacesRequestBuilder setStaleTime(long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public PlacesRequest build(String clientId) {
        final PlacesRequest request = new PlacesRequest(clientId);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
