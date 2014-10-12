package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.dictionary.GameState;
import com.lonebytesoft.thetaleclient.api.dictionary.HeroMode;
import com.lonebytesoft.thetaleclient.api.model.AccountInfo;
import com.lonebytesoft.thetaleclient.api.model.TurnInfo;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 01.10.2014
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
        final JSONObject turnInfoJson = data.getJSONObject("turn");
        turnInfo = new TurnInfo(
                turnInfoJson.getInt("number"),
                turnInfoJson.getString("verbose_date"),
                turnInfoJson.getString("verbose_time"));

        heroMode = ObjectUtils.getEnumForCode(HeroMode.class, data.getString("mode"));
        gameState = ObjectUtils.getEnumForCode(GameState.class, data.getInt("game_state"));
        mapVersion = data.getString("map_version");
        account = ObjectUtils.getModelFromJson(AccountInfo.class, data.optJSONObject("account"));
        enemy = ObjectUtils.getModelFromJson(AccountInfo.class, data.optJSONObject("enemy"));
    }

}
