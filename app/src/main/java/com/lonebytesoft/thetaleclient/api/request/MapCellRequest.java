package com.lonebytesoft.thetaleclient.api.request;

import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonRequest;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapCellRequest extends CommonRequest {

    private static final String URL = "http://the-tale.org/game/map/cell-info";
    private static final boolean SHOULD_PARSE_RESPONSE = false;

    public void execute(final int cellX, final int cellY, final CommonResponseCallback<MapCellResponse, String> callback) {
        final Map<String, String> getParams = new HashMap<>(2);
        getParams.put("x", String.valueOf(cellX));
        getParams.put("y", String.valueOf(cellY));
        execute(URL, HttpMethod.GET, getParams, null, new CommonResponseCallback<String, Throwable>() {
            @Override
            public void processResponse(String response) {
                if(SHOULD_PARSE_RESPONSE) {
                    final String mapCellResponse = response;
                    new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                        @Override
                        public void processResponse(GameInfoResponse response) {
                            new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                                @Override
                                public void processResponse(MapResponse response) {
                                    try {
                                        callback.processResponse(new MapCellResponse(mapCellResponse, response));
                                    } catch (JSONException e) {
                                        callback.processError(e.getLocalizedMessage());
                                    }
                                }

                                @Override
                                public void processError(String error) {
                                    callback.processError(error);
                                }
                            });
                        }

                        @Override
                        public void processError(GameInfoResponse response) {
                            callback.processError(response.errorMessage);
                        }
                    });
                } else {
                    try {
                        callback.processResponse(new MapCellResponse(response, null));
                    } catch (JSONException e) {
                        callback.processError(e.getLocalizedMessage());
                    }
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
