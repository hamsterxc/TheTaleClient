package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.AccountInfoRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 09.04.2015
 */
public class AccountInfoRequestBuilder implements AbstractRequestBuilder<AccountInfoRequest> {

    private int accountId = 0;
    private long staleTime = -1;

    public AccountInfoRequestBuilder setAccountId(final int accountId) {
        this.accountId = accountId;
        return this;
    }

    public AccountInfoRequestBuilder setStaleTime(long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public AccountInfoRequest build(String clientId) {
        final AccountInfoRequest request = new AccountInfoRequest(clientId, accountId);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
