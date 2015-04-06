package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.model.CardInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.04.2015
 */
public class CombineCardsResponse extends AbstractApiResponse {

    public String message;
    public CardInfo card;

    public CombineCardsResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(final JSONObject data) throws JSONException {
        message = data.getString("message");
        card = ObjectUtils.getModelFromJson(CardInfo.class, data.getJSONObject("card"));
    }

}
