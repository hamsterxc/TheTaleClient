package com.lonebytesoft.thetaleclient.apisdk.prerequisite;

import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.interceptor.BaseRequestExecutionInterceptor;
import com.lonebytesoft.thetaleclient.sdkandroid.interceptor.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.interceptor.RequestExecutionInterceptor;
import com.lonebytesoft.thetaleclient.sdkandroid.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class GameInfoPrerequisiteRequest implements PrerequisiteRequest<GameInfoRequest, GameInfoResponse> {

    @Override
    public AbstractRequestBuilder<GameInfoRequest> getRequestBuilder() {
        return new GameInfoRequestBuilder().setStaleTime(0);
    }

    @Override
    public RequestExecutionInterceptor<GameInfoRequest, GameInfoResponse> getRequestExecutionInterceptor() {
        return new BaseRequestExecutionInterceptor<GameInfoRequest, GameInfoResponse>() {
            @Override
            public boolean beforeExecute() {
                return (PreferencesManager.getAccountId() == 0) || TextUtils.isEmpty(PreferencesManager.getMapVersion());
            }

            @Override
            public GameInfoResponse getSuccessResponseAfterSuccess(GameInfoResponse response) {
                if(response.account == null) {
                    PreferencesManager.setAccountId(0);
                    PreferencesManager.setAccountName(null);
                } else if(response.account.isOwnInfo) {
                    PreferencesManager.setAccountId(response.account.accountId);
                }
                PreferencesManager.setMapVersion(response.mapVersion);
                return response;
            }
        };
    }

}
