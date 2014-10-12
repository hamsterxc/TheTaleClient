package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class InfoResponse extends AbstractApiResponse {

    public String dynamicContentUrl;
    public String staticContentUrl;
    public String gameVersion;
    public int turnDelta;
    public int accountId;
    public String accountName;
    public Map<Action, Integer> abilitiesCost;

    public InfoResponse(final String response) throws JSONException {
        super(response);
    }

    protected void parseData(final JSONObject data) throws JSONException {
        dynamicContentUrl = data.getString("dynamic_content");
        staticContentUrl = data.getString("static_content");
        gameVersion = data.getString("game_version");
        turnDelta = data.getInt("turn_delta");
        accountId = data.optInt("account_id", 0);
        accountName = ObjectUtils.getOptionalString(data, "account_name");

        abilitiesCost = new HashMap<>(Action.values().length);
        final JSONObject abilitiesJson = data.getJSONObject("abilities_cost");
        for(final Action action : Action.values()) {
            abilitiesCost.put(action, abilitiesJson.getInt(action.getCode()));
        }
    }

}
