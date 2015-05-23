package com.lonebytesoft.thetaleclient.sdkandroid.request;

import com.lonebytesoft.thetaleclient.sdk.request.AccountsListRequest;
import com.lonebytesoft.thetaleclient.sdkandroid.AbstractRequestBuilder;

/**
 * @author Hamster
 * @since 23.05.2015
 */
public class AccountsListRequestBuilder implements AbstractRequestBuilder<AccountsListRequest> {

    private String prefix = null;
    private int page = 0;
    private long staleTime = -1;

    public AccountsListRequestBuilder setPrefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    public AccountsListRequestBuilder setPage(final int page) {
        this.page = page;
        return this;
    }

    public AccountsListRequestBuilder setStaleTime(final long staleTime) {
        this.staleTime = staleTime;
        return this;
    }

    @Override
    public AccountsListRequest build(String clientId) {
        final AccountsListRequest request = new AccountsListRequest(prefix, page);
        if(staleTime >= 0) {
            request.setStaleTime(staleTime);
        }
        return request;
    }

}
