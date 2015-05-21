package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.AccountsListResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.05.2015
 */
public class AccountsListRequest extends AbstractApiGetRequest<AccountsListResponse> {

    private final String url;

    public AccountsListRequest(final String prefix, final int page) {
        final Map<String, String> getParams = new HashMap<>(2);
        getParams.put("prefix", prefix == null ? "" : prefix);
        getParams.put("page", String.valueOf(page));
        url = RequestUtils.appendGetParams("http://the-tale.org/accounts/", getParams);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public AccountsListResponse execute() throws ApiException, JSONException {
        return new AccountsListResponse(executeRequest());
    }

}
