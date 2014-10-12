package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class CommonResponse extends AbstractApiResponse {

    public CommonResponse(final String response) throws JSONException {
        super(response);
    }

    protected void parseData(final JSONObject data) {
    }

}
