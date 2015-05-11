package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.MapRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 23.04.2015
 */
public class MapRequestBuilder implements AbstractRequestBuilder<MapRequest> {

    private String dynamicContentUrl = null;
    private String mapVersion = null;
    private long staleTime = -1;

    public MapRequestBuilder setDynamicContentUrl(final String dynamicContentUrl) {
        this.dynamicContentUrl = dynamicContentUrl;
        return this;
    }

    public MapRequestBuilder setMapVersion(final String mapVersion) {
        this.mapVersion = mapVersion;
        return this;
    }

    public MapRequestBuilder setStaleTime(final long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public MapRequest build(String clientId) {
        final MapRequest request = new MapRequest(dynamicContentUrl, mapVersion);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
