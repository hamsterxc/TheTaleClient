package com.lonebytesoft.thetaleclient;

import android.text.TextUtils;
import android.util.Pair;

import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellType;
import com.lonebytesoft.thetaleclient.api.model.PlaceCouncilMember;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.MapCellRequest;
import com.lonebytesoft.thetaleclient.api.request.MapRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * @author Hamster
 * @since 27.12.2014
 */
public class ApiTest extends TestCase {

    public void testMapCellRequest() {
        final CountDownLatch latch = new CountDownLatch(1);
        new GameInfoRequest(false).execute(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                    @Override
                    public void processResponse(MapResponse response) {
                        final Queue<Pair<Integer, Integer>> cells = new LinkedList<>();
                        for(int y = 0; y < response.height; y++) {
                            for(int x = 0; x < response.width; x++) {
                                cells.add(Pair.create(x, y));
                            }
                        }
                        startMapCellRequest(cells, latch);
                    }

                    @Override
                    public void processError(String error) {
                        failRequest("Map", error);
                    }
                });
            }

            @Override
            public void processError(GameInfoResponse response) {
                failRequest("GameInfo", response.errorMessage);
            }
        }, false);

        try {
            latch.await();
        } catch(InterruptedException e) {
            fail(String.format("Latch interrupted: %s", e.getMessage()));
        }
    }

    private void verifyMapCellResponse(final String cellDescription, final MapCellResponse response) {
//        Log.v("Testing", cellDescription);
        final String prefix;
        if(TextUtils.isEmpty(cellDescription)) {
            prefix = "";
        } else {
            prefix = cellDescription + ": ";
        }

        assertNotNull(response.type);
        if(response.type == MapCellType.UNKNOWN) {
            fail(prefix + "type unknown");
        }

        assertFalse(prefix + "title empty", TextUtils.isEmpty(response.title));

        if(response.type == MapCellType.PLACE) {
            assertFalse(prefix + "description empty", TextUtils.isEmpty(response.description));

            assertNotNull(prefix + "council null", response.council);
            assertFalse(prefix + "council empty", response.council.size() == 0);
            for(int i = response.council.size() - 1; i >= 0; i--) {
                final PlaceCouncilMember councilMember = response.council.get(i);
                assertFalse(prefix + "council member name empty", TextUtils.isEmpty(councilMember.name));
                assertNotNull(prefix + "council member gender null", councilMember.gender);
                assertNotNull(prefix + "council member race null", councilMember.race);
                assertNotNull(prefix + "council member profession null", councilMember.profession);
                assertNotNull(prefix + "council member proficiency level null", councilMember.proficiencyLevel);
            }
        } else {
            assertNull(prefix + "description not null", response.description);
            assertNull(prefix + "council not null", response.council);
        }

        if((response.type == MapCellType.PLACE) || (response.type == MapCellType.BUILDING)) {
            assertNotNull(prefix + "parameters null", response.parameters);
            assertFalse(prefix + "parameters empty", response.parameters.size() == 0);
            for(final Map.Entry<String, String> parameterEntry : response.parameters.entrySet()) {
                assertFalse(prefix + "parameter key empty", TextUtils.isEmpty(parameterEntry.getKey()));
                assertFalse(prefix + "parameter \"" + parameterEntry.getKey() + "\" is empty",
                        TextUtils.isEmpty(parameterEntry.getValue()));
            }
        } else {
            assertNull(prefix + "parameters not null", response.parameters);
        }

        assertNotNull(prefix + "terrain null", response.terrain);
        assertFalse(prefix + "terrain empty", response.terrain.size() == 0);
        for(final String terrainLine : response.terrain) {
            assertFalse(prefix + "terrain line empty", TextUtils.isEmpty(terrainLine));
        }
    }

    private void startMapCellRequest(final Queue<Pair<Integer, Integer>> queue, final CountDownLatch latch) {
        final Pair<Integer, Integer> cell = queue.poll();
        if(cell == null) {
            latch.countDown();
        } else {
            final String cellDescription = String.format("Map cell (%dx%d)", cell.first, cell.second);
            new MapCellRequest().execute(cell.first, cell.second, new CommonResponseCallback<MapCellResponse, String>() {
                @Override
                public void processResponse(MapCellResponse response) {
                    verifyMapCellResponse(cellDescription, response);
                    startMapCellRequest(queue, latch);
                }

                @Override
                public void processError(String error) {
                    failRequest(cellDescription, error);
                }
            });
        }
    }

    private void failRequest(final String requestName, final String message) {
        fail(String.format("Request \"%s\" failed: %s", requestName, message));
    }

}
