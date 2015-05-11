package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.AuthRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class AuthRequestBuilder implements AbstractRequestBuilder<AuthRequest> {

    private String login = null;
    private String password = null;

    public AuthRequestBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public AuthRequestBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public AuthRequest build(String clientId) {
        return new AuthRequest(clientId, login, password);
    }

}
