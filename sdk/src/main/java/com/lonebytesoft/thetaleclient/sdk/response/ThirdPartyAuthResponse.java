package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 03.04.2015
 */
public class ThirdPartyAuthResponse extends AbstractApiResponse {

    public String nextUrl;

    public ThirdPartyAuthResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(JSONObject data) throws JSONException {
        nextUrl = data.getString("authorisation_page");
    }

}
