package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.dictionary.ThirdPartyAuthState;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 03.04.2015
 */
public class ThirdPartyAuthStateResponse extends AbstractApiResponse {

    public int accountId;
    public String accountName;
    public int sessionExpiry; // when session expires: timestamp, seconds
    public ThirdPartyAuthState authState;

    public ThirdPartyAuthStateResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(final JSONObject data) throws JSONException {
        accountId = data.optInt("account_id", 0);
        accountName = data.optString("account_name", null);
        sessionExpiry = data.getInt("session_expire_at");
        authState = ObjectUtils.getEnumForCode(ThirdPartyAuthState.class, data.getInt("state"));
    }

}
