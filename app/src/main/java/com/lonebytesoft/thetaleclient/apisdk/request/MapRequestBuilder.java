package com.lonebytesoft.thetaleclient.apisdk.request;

import android.content.Context;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.apisdk.prerequisite.GameInfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.request.MapRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

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

    public static void execute(final Context context, final ApiCallback<MapResponse> callback) {
        RequestExecutor.executeOptional(context, new GameInfoPrerequisiteRequest(), new ApiCallback<GameInfoResponse>() {
            @Override
            public void onSuccess(GameInfoResponse response) {
                RequestExecutor.execute(
                        context,
                        new MapRequestBuilder()
                                .setDynamicContentUrl(PreferencesManager.getDynamicContentUrl())
                                .setMapVersion(PreferencesManager.getMapVersion()),
                        callback);
            }

            @Override
            public void onError(AbstractApiResponse response) {
                callback.onError(response);
            }
        });
    }

}
