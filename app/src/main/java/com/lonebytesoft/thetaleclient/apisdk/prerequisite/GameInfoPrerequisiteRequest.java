package com.lonebytesoft.thetaleclient.apisdk.prerequisite;

import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutionInterceptor;
import com.lonebytesoft.thetaleclient.apisdk.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
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
    public RequestExecutionInterceptor<GameInfoResponse> getRequestExecutionInterceptor() {
        return new RequestExecutionInterceptor<GameInfoResponse>() {
            @Override
            public boolean shouldExecute() {
                return (PreferencesManager.getAccountId() != 0) && (!TextUtils.isEmpty(PreferencesManager.getMapVersion()));
            }

            @Override
            public void afterExecution(GameInfoResponse response) {
                if(response.account == null) {
                    PreferencesManager.setAccountId(0);
                    PreferencesManager.setAccountName(null);
                } else if(response.account.isOwnInfo) {
                    PreferencesManager.setAccountId(response.account.accountId);
                }
                PreferencesManager.setMapVersion(response.mapVersion);
            }
        };
    }

}
