package com.lonebytesoft.thetaleclient.apisdk.request;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.apisdk.interceptor.GameInfoRequestCacheInterceptor;
import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public class GameInfoRequestBuilder implements AbstractRequestBuilder<GameInfoRequest> {

    private Integer accountId = null;
    private GameInfoResponse base = null;
    private long staleTime = -1;

    public GameInfoRequestBuilder setAccountId(final Integer accountId) {
        this.accountId = accountId;
        return this;
    }

    public GameInfoRequestBuilder setBase(final GameInfoResponse base) {
        this.base = base;
        return this;
    }

    public GameInfoRequestBuilder setStaleTime(final long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public GameInfoRequest build(String clientId) {
        final GameInfoRequest request = new GameInfoRequest(clientId, accountId, base);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

    public static void executeWatching(final Context context, final ApiCallback<GameInfoResponse> callback) {
        final int watchingAccountId = PreferencesManager.getWatchingAccountId();
        final boolean isForeignAccount = watchingAccountId != 0;

        final GameInfoRequestBuilder requestBuilder = new GameInfoRequestBuilder();
        if(isForeignAccount) {
            requestBuilder.setAccountId(watchingAccountId);
        } else {
            requestBuilder.setBase(PreferencesManager.getGameInfoResponseCache());
        }

        RequestExecutor.execute(
                context,
                requestBuilder,
                isForeignAccount ? null : new GameInfoRequestCacheInterceptor(),
                callback);
    }

}
