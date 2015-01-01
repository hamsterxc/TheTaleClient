package com.lonebytesoft.thetaleclient.api.cache.prerequisite;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

/**
 * @author Hamster
 * @since 01.01.2015
 */
public class InfoPrerequisiteRequest extends PrerequisiteRequest<InfoResponse> {

    public InfoPrerequisiteRequest(Runnable task, ErrorCallback<InfoResponse> errorCallback, Fragment fragment) {
        super(task, errorCallback, fragment);
    }

    @Override
    protected boolean isPreExecuted() {
        boolean isPreExecuted = (PreferencesManager.getAccountId() != 0) && (!TextUtils.isEmpty(PreferencesManager.getAccountName()));
        for(final Action action : Action.values()) {
            isPreExecuted &= PreferencesManager.getAbilityCost(action) >= 0;
        }
        return isPreExecuted;
    }

    @Override
    protected void preExecuteAndRun() {
        new InfoRequest().execute(getApiCallback());
    }
}
