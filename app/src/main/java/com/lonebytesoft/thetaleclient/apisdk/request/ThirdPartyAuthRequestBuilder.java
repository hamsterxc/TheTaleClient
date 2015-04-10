package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.ThirdPartyAuthRequest;

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

    @Override
    public ThirdPartyAuthRequest build(String clientId) {
        return new ThirdPartyAuthRequest(clientId, applicationName, applicationInfo, applicationDescription);
    }

}
