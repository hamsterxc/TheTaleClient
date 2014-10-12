package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.cache.RequestCacheManager;
import com.lonebytesoft.thetaleclient.api.response.AuthResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class AuthRequest extends AbstractApiRequest<AuthResponse> {

    public AuthRequest() {
        super(HttpMethod.POST, "accounts/auth/api/login", "1.0", true);
    }

    public void execute(final String email, final String password, final boolean remember,
                        final ApiResponseCallback<AuthResponse> callback) {
        final Map<String, String> postParams = new HashMap<>(3);
        postParams.put("email", email);
        postParams.put("password", password);
        postParams.put("remember", String.valueOf(remember));
        execute(null, postParams, callback);
    }

    protected AuthResponse getResponse(final String response) throws JSONException {
        RequestCacheManager.invalidate();
        return new AuthResponse(response);
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
