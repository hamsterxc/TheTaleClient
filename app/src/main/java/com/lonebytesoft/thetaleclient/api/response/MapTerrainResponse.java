package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.model.MapCellTerrainInfo;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 18.10.2014
 */
public class MapTerrainResponse {

    public final List<List<MapCellTerrainInfo>> cells;

    public MapTerrainResponse(final String response) throws JSONException {
        final JSONArray json = new JSONArray(response);
        final int rowsCount = json.length();
        cells = new ArrayList<>(rowsCount);
        for(int y = 0; y < rowsCount; y++) {
            final JSONArray rowJson = json.getJSONArray(y);
            final int cellsCount = rowJson.length();
            final List<MapCellTerrainInfo> cellRow = new ArrayList<>(cellsCount);
            for(int x = 0; x < cellsCount; x++) {
                cellRow.add(ObjectUtils.getModelFromJson(MapCellTerrainInfo.class, rowJson.getJSONObject(x)));
            }
            cells.add(cellRow);
        }
    }

}
