package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public class MapTileInfo {

    public final int spriteTileId;
    public final int rotation; // degrees

    public MapTileInfo(final JSONObject json) throws JSONException {
        spriteTileId = json.getInt("spriteTileId");
        rotation = json.getInt("rotation");
    }

}
