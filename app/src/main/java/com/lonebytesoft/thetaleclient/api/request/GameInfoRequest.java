package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.AbstractApiRequest;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.ApiResponseStatus;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public class GameInfoRequest extends AbstractApiRequest<GameInfoResponse> {

    private final boolean needAuthorization;

    public GameInfoRequest(final boolean needAuthorization) {
        super(HttpMethod.GET, "game/api/info", "1.1", true);
        this.needAuthorization = needAuthorization;
    }

    public void execute(final int accountId, final ApiResponseCallback<GameInfoResponse> callback,
                        final boolean useCache) {
        final Map<String, String> getParams = new HashMap<>(1);
        getParams.put("account", String.valueOf(accountId));
        execute(getParams, null, callback, useCache);
    }

    public void execute(final ApiResponseCallback<GameInfoResponse> callback, final boolean useCache) {
        execute(null, null, callback, useCache);
    }

    protected GameInfoResponse getResponse(final String response) throws JSONException {
        final GameInfoResponse gameInfoResponse = new GameInfoResponse(response);

        if(gameInfoResponse.account != null) {
            PreferencesManager.setAccountId(gameInfoResponse.account.accountId);
        }

        if((gameInfoResponse.status == ApiResponseStatus.OK) && (gameInfoResponse.account == null) && needAuthorization) {
            return new GameInfoResponse(RequestUtils.getGenericErrorResponse(
                    TheTaleClientApplication.getContext().getString(R.string.game_not_authorized)));
        } else {
            return gameInfoResponse;
        }
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
