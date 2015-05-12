package com.lonebytesoft.thetaleclient.sdkandroid.request;

import android.content.Context;
import android.os.Build;

import com.lonebytesoft.thetaleclient.sdk.request.ThirdPartyAuthRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.BuildConfig;
import com.lonebytesoft.thetaleclient.sdkandroid.R;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class ThirdPartyAuthRequestBuilder implements AbstractRequestBuilder<ThirdPartyAuthRequest> {

    private String applicationName = null;
    private String applicationInfo = null;
    private String applicationDescription = null;

    public ThirdPartyAuthRequestBuilder setApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public ThirdPartyAuthRequestBuilder setApplicationInfo(String applicationInfo) {
        this.applicationInfo = applicationInfo;
        return this;
    }

    public ThirdPartyAuthRequestBuilder setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
        return this;
    }

    public ThirdPartyAuthRequestBuilder setDefaults(final Context context) {
        this.applicationName = context.getString(R.string.auth_app_name);
        this.applicationInfo = String.format("%s %s, %s %s (%d)",
                Build.BRAND, Build.MODEL,
                context.getPackageName(), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        this.applicationDescription = context.getString(R.string.auth_app_description);
        return this;
    }

    @Override
    public ThirdPartyAuthRequest build(String clientId) {
        return new ThirdPartyAuthRequest(clientId, applicationName, applicationInfo, applicationDescription);
    }

}
