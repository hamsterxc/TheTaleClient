package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.ThirdPartyAuthResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 03.04.2015
 */
public class ThirdPartyAuthRequest extends AbstractRequest<ThirdPartyAuthResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "accounts/third-party/tokens/api/request-authorisation";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public ThirdPartyAuthRequest(final String clientId, final String applicationName,
                                 final String applicationInfo, final String applicationDescription) {
        url = RequestUtils.getApiUrl(METHOD);

        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);

        postParams = new HashMap<>();
        postParams.put("application_name", applicationName);
        postParams.put("application_info", applicationInfo);
        postParams.put("application_description", applicationDescription);
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(String csrfToken) {
        postParams.put(RequestUtils.PARAM_POST_CSRF_TOKEN, csrfToken);
        return RequestUtils.getHttpPostRequestWithParams(url, getParams, postParams);
    }

    public ThirdPartyAuthResponse execute() throws ApiException, JSONException {
        return new ThirdPartyAuthResponse(executeRequest());
    }

}
