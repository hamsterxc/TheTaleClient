package com.lonebytesoft.thetaleclient.apisdk.interceptor;

import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 17.04.2015
 */
public class GameInfoRequestCacheInterceptor extends BaseRequestExecutionInterceptor<GameInfoRequest, GameInfoResponse> {

    @Override
    public GameInfoResponse getSuccessResponseAfterSuccess(GameInfoResponse response) {
        PreferencesManager.setGameInfoResponseCache(response);
        return response;
    }

}
