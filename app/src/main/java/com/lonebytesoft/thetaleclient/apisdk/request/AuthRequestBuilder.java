package com.lonebytesoft.thetaleclient.apisdk.request;

import com.lonebytesoft.thetaleclient.apisdk.AbstractRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.request.AuthRequest;

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
