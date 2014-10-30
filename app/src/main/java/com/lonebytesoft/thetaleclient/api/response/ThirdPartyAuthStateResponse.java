package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.dictionary.ThirdPartyAuthState;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 29.10.2014
 */
public class ThirdPartyAuthStateResponse extends AbstractApiResponse {

    public ThirdPartyAuthState authState;

    public ThirdPartyAuthStateResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(final JSONObject data) throws JSONException {
        authState = ObjectUtils.getEnumForCode(ThirdPartyAuthState.class, data.getInt("state"));
    }

}
