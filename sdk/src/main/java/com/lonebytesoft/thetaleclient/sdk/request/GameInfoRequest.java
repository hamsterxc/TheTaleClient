package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.util.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class GameInfoRequest extends AbstractApiGetRequest<GameInfoResponse> {

    private static final String VERSION = "1.3";
    private static final String METHOD = "game/api/info";
    private static final long STALE_TIME = 10000; // 10 seconds

    private final String url;
    private final GameInfoResponse base;

    public GameInfoRequest(final String clientId) {
        this(clientId, null, null);
    }

    public GameInfoRequest(final String clientId, final GameInfoResponse base) {
        this(clientId, null, base);
    }

    public GameInfoRequest(final String clientId, final int accountId) {
        this(clientId, accountId, null);
    }

    public GameInfoRequest(final String clientId, final Integer accountId, final GameInfoResponse base) {
        final Map<String, String> getParams = RequestUtils.getApiMethodGetParams(VERSION, clientId);

        if(accountId != null) {
            getParams.put("account", String.valueOf(accountId));
        }

        if((base != null) && (base.account != null)) {
            getParams.put("client_turns", String.valueOf(base.account.hero.actualityTurnNumber));
            this.base = base;
        } else {
            this.base = null;
        }

        url = RequestUtils.appendGetParams(RequestUtils.getApiUrl(METHOD), getParams);

        setStaleTime(STALE_TIME);
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    public GameInfoResponse execute() throws ApiException, JSONException {
        String response = executeRequest();

        // merge known base into received diff
        if(base != null) {
            final JSONObject json = new JSONObject(response);
            final JSONObject data = json.getJSONObject("data");
            final JSONObject jsonBase = new JSONObject(this.base.rawResponse);
            final JSONObject dataBase = jsonBase.getJSONObject("data");

            mergeHeroInfo(data, dataBase, "account");
            mergeHeroInfo(data, dataBase, "enemy");

            response = json.toString();
        }

        return new GameInfoResponse(response);
    }

    private void mergeHeroInfo(final JSONObject data, final JSONObject dataBase, final String accountKey) {
        if(data.isNull(accountKey) || dataBase.isNull(accountKey)) {
            return;
        }

        final JSONObject hero = data.getJSONObject(accountKey).getJSONObject("hero");
        final JSONObject heroBase = dataBase.getJSONObject(accountKey).getJSONObject("hero");

        for(final Iterator<String> iterator = heroBase.keys(); iterator.hasNext();) {
            final String key = iterator.next();
            if(!hero.has(key)) {
                hero.put(key, heroBase.get(key));
            }
        }
    }

}
