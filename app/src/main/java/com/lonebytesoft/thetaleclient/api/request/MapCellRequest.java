package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.CommonRequest;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapCellRequest extends CommonRequest {

    private static final String URL = "http://the-tale.org/game/map/cell-info";

    public void execute(final int cellX, final int cellY, final CommonResponseCallback<MapCellResponse, String> callback) {
        final Map<String, String> getParams = new HashMap<>(2);
        getParams.put("x", String.valueOf(cellX));
        getParams.put("y", String.valueOf(cellY));
        execute(URL, HttpMethod.GET, getParams, null, new CommonResponseCallback<String, Throwable>() {
            @Override
            public void processResponse(String response) {
                try {
                    callback.processResponse(new MapCellResponse(response));
                } catch (JSONException e) {
                    callback.processError(e.getLocalizedMessage());
                }
            }

            @Override
            public void processError(Throwable error) {
                callback.processError(error.getLocalizedMessage());
            }
        });
    }

    @Override
    protected long getStaleTime() {
        return 60000;
    }

}
