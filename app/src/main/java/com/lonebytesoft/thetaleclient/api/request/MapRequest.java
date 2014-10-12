package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.CommonRequest;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;

import org.json.JSONException;

/**
 * @author Hamster
 * @since 07.10.2014
 * todo undocumented request
 */
public class MapRequest extends CommonRequest {

    private static final String URL = "http://the-tale.org/dcont/map/region-%s.js";

    private final String mapVersion;

    public MapRequest(final String mapVersion) {
        this.mapVersion = mapVersion;
    }

    public void execute(final CommonResponseCallback<MapResponse, String> callback) {
        execute(String.format(URL, mapVersion), HttpMethod.GET, null, null, new CommonResponseCallback<String, Throwable>() {
            @Override
            public void processResponse(String response) {
                try {
                    callback.processResponse(new MapResponse(response));
                } catch(JSONException e) {
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
