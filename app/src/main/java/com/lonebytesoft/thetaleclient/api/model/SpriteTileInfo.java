package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONObject;

/**
 * @author Hamster
 * @since 13.10.2014
 */
public class SpriteTileInfo {

    public final int x;
    public final int y;
    public final int rotation;
    public final int size;

    public SpriteTileInfo(final int x, final int y, final int rotation, final int size) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.size = size;
    }

    public SpriteTileInfo(final JSONObject json) {
        throw new UnsupportedOperationException("SpriteTileInfo cannot be instantiated using JSON");
    }

}
