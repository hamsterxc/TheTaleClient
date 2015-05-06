package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdk.model.MapPlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MapTileInfo;
import com.lonebytesoft.thetaleclient.sdk.model.RoadInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public class MapResponse extends AbstractResponse {

    public final String versionFormat;
    public final String versionMap;

    public final int width;
    public final int height;

    public final Map<Integer, MapPlaceInfo> places;
    public final Map<Integer, RoadInfo> roads;
    public final List<List<List<MapTileInfo>>> tiles;

    public MapResponse(final String response) throws JSONException {
        super(response);
        final JSONObject json = new JSONObject(response);

        versionFormat = json.getString("format_version");
        versionMap = json.getString("map_version");

        width = json.getInt("width");
        height = json.getInt("height");

        final JSONObject placesJson = json.getJSONObject("places");
        places = new HashMap<>(placesJson.length());
        for(final Iterator<String> placesIterator = placesJson.keys(); placesIterator.hasNext();) {
            final String key = placesIterator.next();
            places.put(Integer.decode(key), ObjectUtils.getModelFromJson(MapPlaceInfo.class, placesJson.getJSONObject(key)));
        }

        final JSONObject roadsJson = json.getJSONObject("roads");
        roads = new HashMap<>(roadsJson.length());
        for(final Iterator<String> roadsIterator = roadsJson.keys(); roadsIterator.hasNext();) {
            final String key = roadsIterator.next();
            roads.put(Integer.decode(key), ObjectUtils.getModelFromJson(RoadInfo.class, roadsJson.getJSONObject(key)));
        }

        final JSONArray drawJson = json.getJSONArray("draw_info");
        final int rowsCount = drawJson.length();
        tiles = new ArrayList<>(rowsCount);
        for(int i = 0; i < rowsCount; i++) {
            final JSONArray rowJson = drawJson.getJSONArray(i);
            final int columnsCount = rowJson.length();
            final List<List<MapTileInfo>> row = new ArrayList<>(columnsCount);
            for(int j = 0; j < columnsCount; j++) {
                final JSONArray cellJson = rowJson.getJSONArray(j);
                final int tilesCount = cellJson.length();
                final List<MapTileInfo> cell = new ArrayList<>(tilesCount);
                for(int k = 0; k < tilesCount; k++) {
                    final JSONArray tileJson = cellJson.getJSONArray(k);
                    cell.add(new MapTileInfo(ObjectUtils.getObjectFromArray(tileJson,
                            new String[]{"spriteTileId", "rotation"})));
                }
                row.add(cell);
            }
            tiles.add(row);
        }
    }

}
