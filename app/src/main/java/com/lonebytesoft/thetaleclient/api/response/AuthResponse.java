package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class AuthResponse extends AbstractApiResponse {

    public String nextUrl;
    public int accountId;
    public String accountName;
    public int sessionExpiry;

    public List<String> errorsLogin = null;
    public List<String> errorsPassword = null;

    public AuthResponse(final String response) throws JSONException {
        super(response);

        if(errors != null) {
            errorsLogin = errors.get("email");
            errorsPassword = errors.get("password");
        }
    }

    protected void parseData(final JSONObject data) throws JSONException {
        nextUrl = data.getString("next_url");
        accountId = data.getInt("account_id");
        accountName = data.getString("account_name");
        sessionExpiry = data.getInt("session_expire_at");
    }

}
