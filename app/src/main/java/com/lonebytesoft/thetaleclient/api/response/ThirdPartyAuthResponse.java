package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 29.10.2014
 */
public class ThirdPartyAuthResponse extends AbstractApiResponse {

    public String nextUrl;

    public ThirdPartyAuthResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(final JSONObject data) throws JSONException {
        nextUrl = data.getString("authorisation_page");
    }

}
