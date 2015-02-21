package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.AccountInfoResponse;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 20.02.2015
 */
public class AccountInfoRequest extends AbstractApiRequest<AccountInfoResponse> {

    public AccountInfoRequest(final int accountId) {
        super(HttpMethod.GET, String.format("accounts/%d/api/show", accountId), "1.0", true);
    }

    public void execute(final ApiResponseCallback<AccountInfoResponse> callback) {
        execute(null, null, callback);
    }

    @Override
    protected AccountInfoResponse getResponse(String response) throws JSONException {
        return new AccountInfoResponse(response);
    }

    @Override
    protected long getStaleTime() {
        return 7200; // 2 hours
    }

}
