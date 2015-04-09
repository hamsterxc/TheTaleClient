package com.lonebytesoft.thetaleclient.apisdk;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.ApiResponseStatus;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONException;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONObject;

/**
 * @author Hamster
 * @since 07.04.2015
 */
public class ErrorApiResponse extends AbstractApiResponse {

    public ErrorApiResponse(final String message) throws JSONException {
        super(getJson(message));
    }

    @Override
    protected void parseData(JSONObject data) throws JSONException {
    }

    private static String getJson(final String message) throws JSONException {
        final JSONObject json = new JSONObject();
        json.put("status", ApiResponseStatus.ERROR.code);
        json.put("code", ApiResponseStatus.ERROR.code);
        json.put("error", message);
        return json.toString();
    }

}
