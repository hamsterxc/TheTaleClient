package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class GameInfoRequest extends AbstractApiRequest<GameInfoResponse> {

    public GameInfoRequest() {
        super(HttpMethod.GET, "game/api/info", "1.1", true);
    }

    public void execute(final int accountId, final ApiResponseCallback<GameInfoResponse> callback) {
        final Map<String, String> getParams = new HashMap<>(1);
        getParams.put("account", String.valueOf(accountId));
        execute(getParams, null, callback);
    }

    public void execute(final ApiResponseCallback<GameInfoResponse> callback) {
        execute(null, null, callback);
    }

    protected GameInfoResponse getResponse(final String response) throws JSONException {
        return new GameInfoResponse(response);
    }

    @Override
    protected boolean isFinished(final GameInfoResponse response) {
        return super.isFinished(response) && ((response == null) ||
                ((response.account == null) || !response.account.isObsoleteInfo) &&
                ((response.enemy == null) || !response.enemy.isObsoleteInfo));
    }

    @Override
    protected long getStaleTime() {
        return 5000;
    }

}
