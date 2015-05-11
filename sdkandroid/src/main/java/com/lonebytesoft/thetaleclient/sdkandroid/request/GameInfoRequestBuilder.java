package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

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

}
