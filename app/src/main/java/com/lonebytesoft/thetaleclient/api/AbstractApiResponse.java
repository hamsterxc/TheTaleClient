package com.lonebytesoft.thetaleclient.api;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 30.09.2014
 */
public abstract class AbstractApiResponse {

    public final String rawResponse;

    public final boolean isDeprecated;
    public final ApiResponseStatus status;

    public final String errorCode;
    public final String errorMessage;
    public final Map<String, List<String>> errors;

    public final String statusUrl;

    public AbstractApiResponse(final String response) throws JSONException {
        rawResponse = response;
        JSONObject json;
        try {
            json = new JSONObject(response);
        } catch(JSONException e) {
            json = new JSONObject(RequestUtils.getGenericErrorResponse(
                    TheTaleClientApplication.getContext().getString(R.string.common_error)));
        }

        isDeprecated = json.optBoolean("depricated");
        status = ObjectUtils.getEnumForCode(ApiResponseStatus.class, json.getString("status"));
        statusUrl = json.optString("status_url");

        errorCode = json.optString("code");
        errorMessage = json.optString("error");
        final JSONObject errorsJson = json.optJSONObject("errors");
        if(errorsJson == null) {
            errors = null;
        } else {
            errors = new HashMap<>(errorsJson.length());
            for(final Iterator<String> keysIterator = errorsJson.keys(); keysIterator.hasNext();) {
                final String key = keysIterator.next();
                final JSONArray fieldErrorsJson = errorsJson.getJSONArray(key);
                final int fieldErrorsLength = fieldErrorsJson.length();
                final List<String> fieldErrors = new ArrayList<>(fieldErrorsLength);
                for(int i = 0; i < fieldErrorsLength; i++) {
                    fieldErrors.add(fieldErrorsJson.getString(i));
                }
                errors.put(key, fieldErrors);
            }
        }

        if(status == ApiResponseStatus.OK) {
            if(json.has("data")) {
                parseData(json.getJSONObject("data"));
            }
        }
    }

    protected abstract void parseData(final JSONObject data) throws JSONException;

}
