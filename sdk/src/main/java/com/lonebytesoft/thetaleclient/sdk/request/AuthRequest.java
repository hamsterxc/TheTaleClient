package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.AuthResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class AuthRequest extends AbstractRequest<AuthResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "accounts/auth/api/login";

    final String url;
    final Map<String, String> getParams;
    final Map<String, String> postParams;

    public AuthRequest(final String clientId, final String login, final String password) {
        url = RequestUtils.getApiUrl(METHOD);

        getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);

        postParams = new HashMap<>();
        postParams.put("email", login);
        postParams.put("password", password);
        postParams.put("remember", String.valueOf(true));
    }

    @Override
    protected HttpUriRequest getHttpUriRequest(String csrfToken) {
        postParams.put(RequestUtils.PARAM_POST_CSRF_TOKEN, csrfToken);
        return RequestUtils.getHttpPostRequestWithParams(url, getParams, postParams);
    }

    public AuthResponse execute() throws ApiException, JSONException {
        final String response = executeRequest();
        return new AuthResponse(response);
    }

}
