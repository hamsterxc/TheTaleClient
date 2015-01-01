package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.dictionary.ThirdPartyAuthState;
import com.lonebytesoft.thetaleclient.api.response.ThirdPartyAuthStateResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 29.10.2014
 */
public class ThirdPartyAuthStateRequest extends AbstractApiRequest<ThirdPartyAuthStateResponse> {

    public ThirdPartyAuthStateRequest() {
        super(HttpMethod.GET, "accounts/third-party/tokens/api/authorisation-state", "1.0", true);
    }

    public void execute(final ApiResponseCallback<ThirdPartyAuthStateResponse> callback) {
        execute(null, null, callback);
    }

    @Override
    protected ThirdPartyAuthStateResponse getResponse(final String response) throws JSONException {
        final ThirdPartyAuthStateResponse thirdPartyAuthStateResponse = new ThirdPartyAuthStateResponse(response);

        if(thirdPartyAuthStateResponse.authState == ThirdPartyAuthState.SUCCESS) {
            PreferencesManager.setAccountId(thirdPartyAuthStateResponse.accountId);
            PreferencesManager.setAccountName(thirdPartyAuthStateResponse.accountName);
        } else {
            PreferencesManager.setAccountId(0);
            PreferencesManager.setAccountName(null);
        }

        return thirdPartyAuthStateResponse;
    }

    @Override
    protected long getStaleTime() {
        return 0;
    }

}
