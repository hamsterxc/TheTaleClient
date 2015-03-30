package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 19.03.2015
 */
public class CommonResponse extends AbstractApiResponse {

    public CommonResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(final JSONObject data) throws JSONException {
    }

}
