package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.GameState;
import com.lonebytesoft.thetaleclient.sdk.dictionary.HeroMode;
import com.lonebytesoft.thetaleclient.sdk.model.AccountInfo;
import com.lonebytesoft.thetaleclient.sdk.model.TurnInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public class GameInfoResponse extends AbstractApiResponse {

    public HeroMode heroMode;
    public TurnInfo turnInfo;
    public GameState gameState;
    public String mapVersion;
    public AccountInfo account;
    public AccountInfo enemy;

    public GameInfoResponse(final String response) throws JSONException {
        super(response);
    }

    protected void parseData(final JSONObject data) throws JSONException {
        turnInfo = ObjectUtils.getModelFromJson(TurnInfo.class, data.getJSONObject("turn"));
        heroMode = ObjectUtils.getEnumForCode(HeroMode.class, data.getString("mode"));
        gameState = ObjectUtils.getEnumForCode(GameState.class, data.getInt("game_state"));
        mapVersion = data.getString("map_version");
        account = ObjectUtils.getModelFromJson(AccountInfo.class, data.optJSONObject("account"));
        enemy = ObjectUtils.getModelFromJson(AccountInfo.class, data.optJSONObject("enemy"));
    }

}
