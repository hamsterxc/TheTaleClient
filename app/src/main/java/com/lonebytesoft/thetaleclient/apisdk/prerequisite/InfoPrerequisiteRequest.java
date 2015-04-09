package com.lonebytesoft.thetaleclient.apisdk.prerequisite;

import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutionInterceptor;
import com.lonebytesoft.thetaleclient.apisdk.request.InfoRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.InfoRequest;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class InfoPrerequisiteRequest implements PrerequisiteRequest<InfoRequest, InfoResponse> {

    @Override
    public AbstractRequestBuilder<InfoRequest> getRequestBuilder() {
        return new InfoRequestBuilder();
    }

    @Override
    public RequestExecutionInterceptor<InfoResponse> getRequestExecutionInterceptor() {
        return new RequestExecutionInterceptor<InfoResponse>() {
            @Override
            public boolean shouldExecute() {
                boolean shouldExecute =
                        (PreferencesManager.getDynamicContentUrl() != null)
                        && (PreferencesManager.getStaticContentUrl() != null)
                        && (PreferencesManager.getTurnDelta() > 0)
                        && (PreferencesManager.getAccountId() != 0)
                        && (!TextUtils.isEmpty(PreferencesManager.getAccountName()));
                for(final Action action : Action.values()) {
                    shouldExecute &= PreferencesManager.getAbilityCost(action) >= 0;
                }
                return shouldExecute;
            }

            @Override
            public void afterExecution(InfoResponse response) {
                PreferencesManager.setDynamicContentUrl(response.dynamicContentUrl);
                PreferencesManager.setStaticContentUrl(response.staticContentUrl);
                PreferencesManager.setTurnDelta(response.turnDelta);
                PreferencesManager.setAccountId(response.accountId);
                PreferencesManager.setAccountName(response.accountName);

                // todo rework after full move to SDK
//                PreferencesManager.setAbilitiesCost(response.abilitiesCost);
                final Map<Action, Integer> abilitiesCost = new HashMap<>();
                for(final Action action : Action.values()) {
                    abilitiesCost.put(action, response.abilitiesCost.get(ObjectUtils.getEnumForCode(
                            com.lonebytesoft.thetaleclient.sdk.dictionary.Action.class, action.getCode())));
                }
                PreferencesManager.setAbilitiesCost(abilitiesCost);
            }
        };
    }

}
