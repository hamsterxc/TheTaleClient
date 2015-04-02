package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.AccountInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;

import java.util.Map;

/**
 * @author Hamster
 * @since 23.03.2015
 */
public class AccountInfoRequest extends AbstractApiGetRequest<AccountInfoResponse> {

    private static final String VERSION = "1.0";
    private static final String METHOD = "accounts/%d/api/show";
    private static final long STALE_TIME = 60000; // 1 minute

    private final String url;

    public AccountInfoRequest(final String clientId, final int accountId) {
        final Map<String, String> getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);
        url = RequestUtils.appendGetParams(RequestUtils.getApiUrl(String.format(METHOD, accountId)), getParams);
        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public AccountInfoResponse execute() throws ApiException, JSONException {
        return new AccountInfoResponse(executeRequest());
    }

}
