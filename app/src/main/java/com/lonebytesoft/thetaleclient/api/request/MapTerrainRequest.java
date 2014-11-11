package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.CommonRequest;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.MapTerrainResponse;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 18.10.2014
 */
public class MapTerrainRequest extends CommonRequest {

    private static final String URL = "http://lonebytesoft.com:15934/the-tale/map_data.js";

    public void execute(final CommonResponseCallback<MapTerrainResponse, String> callback) {
        execute(URL, HttpMethod.GET, null, null, new CommonResponseCallback<String, Throwable>() {
            @Override
            public void processResponse(String response) {
                try {
                    RequestUtils.processResultInMainThread(callback, false, new MapTerrainResponse(response), null);
                } catch (JSONException e) {
                    RequestUtils.processResultInMainThread(callback, true, null, e.getLocalizedMessage());
                }
            }

            @Override
            public void processError(Throwable error) {
                RequestUtils.processResultInMainThread(callback, true, null, error.getLocalizedMessage());
            }
        });
    }

    @Override
    protected long getStaleTime() {
        return 60000;
    }

}
