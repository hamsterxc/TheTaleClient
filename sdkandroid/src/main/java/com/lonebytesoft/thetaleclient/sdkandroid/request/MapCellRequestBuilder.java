package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.MapCellRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 30.04.2015
 */
public class MapCellRequestBuilder implements AbstractRequestBuilder<MapCellRequest> {

    private int x = -1;
    private int y = -1;
    private long staleTime = -1;

    public MapCellRequestBuilder setX(final int x) {
        this.x = x;
        return this;
    }

    public MapCellRequestBuilder setY(final int y) {
        this.y = y;
        return this;
    }

    public MapCellRequestBuilder setStaleTime(final long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public MapCellRequest build(String clientId) {
        final MapCellRequest request = new MapCellRequest(x, y);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
