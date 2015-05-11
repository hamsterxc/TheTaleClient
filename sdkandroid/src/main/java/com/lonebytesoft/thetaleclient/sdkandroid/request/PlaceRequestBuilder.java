package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.PlaceRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 07.05.2015
 */
public class PlaceRequestBuilder implements AbstractRequestBuilder<PlaceRequest> {

    private int placeId = 0;
    private long staleTime = -1;

    public PlaceRequestBuilder setPlaceId(final int placeId) {
        this.placeId = placeId;
        return this;
    }

    public PlaceRequestBuilder setStaleTime(long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public PlaceRequest build(String clientId) {
        final PlaceRequest request = new PlaceRequest(clientId, placeId);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
