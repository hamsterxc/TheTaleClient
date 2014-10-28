package com.lonebytesoft.thetaleclient.util.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.api.model.HeroInfo;
import com.lonebytesoft.thetaleclient.api.model.MapCellTerrainInfo;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.api.model.SpriteTileInfo;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;
import com.lonebytesoft.thetaleclient.api.response.MapTerrainResponse;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.10.2014
 */
public class MapManager {

    private static final String MAP_SPRITE_URL = "http:%s%s";
    public static final int MAP_TILE_SIZE = 32;
    private static final int HERO_SPRITE_SHIFT_Y = -12;

    private static final String PLACE_CAPTION = "(%d) %s";
    private static final float PLACE_TEXT_SIZE = 12f;
    private static final float PLACE_TEXT_SIZE_MIN = 8f;
    private static final float PLACE_TEXT_X_SHIFT = -2f;
    private static final float PLACE_TEXT_Y_SHIFT = 12f;
    private static final float PLACE_TEXT_BACKGROUND_PADDING = 2f;

    private static final int[] MAP_SPRITE_OFFSET_X = new int[]{
            0, 32, 64, 96, 128, 160, 192, 224, 256, 288,
            0, 64, 128, 192, 256, 128, 96, 32, 0, 32,
            96, 128, 160, 0, 64, 0, 32, 64, 96, 160,
            224, 352, 320, 288, 192, 256, 192, 224, 256, 288,
            192, 224, 64, 352, 0, 32, 64, 96, 128, 160,
            192, 224, 256, 288, 320, 352, 0, 32, 64, 96,
            128, 160, 192, 224, 96, 32, 160, 64, 128, 0,
            0, 32, 0, 32, 64, 96, 128, 160, 192, 224,
            256, 288, 320, 352, 0, 32, 64, 96, 128, 160,
            192, 224, 256, 288};
    private static final int[] MAP_SPRITE_OFFSET_Y = new int[]{
            256, 256, 256, 256, 256, 256, 256, 256, 256, 256,
            256, 256, 256, 256, 256, 64, 64, 64, 64, 0,
            0, 0, 0, 0, 0, 32, 32, 32, 32, 64,
            0, 0, 0, 0, 0, 0, 32, 32, 32, 32,
            64, 64, 64, 32, 192, 192, 192, 192, 192, 192,
            192, 192, 192, 192, 192, 192, 224, 224, 224, 224,
            224, 224, 224, 224, 96, 96, 96, 96, 96, 96,
            288, 288, 128, 128, 128, 128, 128, 128, 128, 128,
            128, 128, 128, 128, 160, 160, 160, 160, 160, 160,
            160, 160, 160, 160};

    private static final Map<MapStyle, Bitmap> mapSpriteCache = new HashMap<>(MapStyle.values().length);

    private static int sizeDenominator = 1;

    /**
     * Returns a bitmap to draw a map on
     * @param mapInfo map information
     * @return empty bitmap of necessary size
     */
    public static Bitmap getMapBitmap(final MapResponse mapInfo) {
        for(sizeDenominator = 1;; sizeDenominator *= 2) {
            final long size = (mapInfo.width * MAP_TILE_SIZE / sizeDenominator) *
                    (mapInfo.height * MAP_TILE_SIZE / sizeDenominator) * 4;
            if(size < TheTaleClientApplication.getFreeMemory() * 0.9) {
                return Bitmap.createBitmap(mapInfo.width * MAP_TILE_SIZE / sizeDenominator,
                        mapInfo.height * MAP_TILE_SIZE / sizeDenominator, Bitmap.Config.ARGB_8888);
            }
        }
    }

    /**
     * Synchronously draws a base layer of the map (terrain, places, buildings)
     * @param canvas a canvas for the map bitmap to draw layer on
     * @param mapInfo map information
     * @param sprite map sprite
     */
    public static void drawBaseLayer(final Canvas canvas, final MapResponse mapInfo, final Bitmap sprite, final List<ExcludeTileInfo> excludeTiles) {
        final Rect tileRect = new Rect(0, 0, MAP_TILE_SIZE, MAP_TILE_SIZE);
        final int rowsCount = mapInfo.tiles.size();
        for(int i = 0; i < rowsCount; i++) {
            final List<List<SpriteTileInfo>> row = mapInfo.tiles.get(i);
            final int cellsCount = row.size();
            for(int j = 0; j < cellsCount; j++) {
                final Rect dst = new Rect(
                        j * MAP_TILE_SIZE / sizeDenominator, i * MAP_TILE_SIZE / sizeDenominator,
                        (j + 1) * MAP_TILE_SIZE / sizeDenominator, (i + 1) * MAP_TILE_SIZE / sizeDenominator);
                final List<SpriteTileInfo> tiles = row.get(j);
                final int tilesCount = tiles.size();
                for(int index = 0; index < tilesCount; index++) {
                    boolean exclude = false;
                    if(excludeTiles != null) {
                        for (final ExcludeTileInfo excludeTileInfo : excludeTiles) {
                            if ((i == excludeTileInfo.y) && (j == excludeTileInfo.x) && (index == excludeTileInfo.index)) {
                                exclude = true;
                                break;
                            }
                        }
                    }
                    if(exclude) {
                        continue;
                    }

                    final SpriteTileInfo tile = tiles.get(index);
                    if(tile.rotation == 0) {
                        final Rect src = new Rect(
                                tile.x, tile.y,
                                tile.x + tile.size, tile.y + tile.size);
                        canvas.drawBitmap(sprite, src, dst, null);
                    } else {
                        final Matrix rotationMatrix = new Matrix();
                        rotationMatrix.setRotate(tile.rotation);
                        final Bitmap rotatingTileBitmap = Bitmap.createBitmap(
                                sprite,
                                tile.x, tile.y,
                                MAP_TILE_SIZE, MAP_TILE_SIZE,
                                rotationMatrix, true);
                        canvas.drawBitmap(rotatingTileBitmap, tileRect, dst, null);
                    }
                }
            }
        }
    }

    /**
     * Asynchronously gets an appropriate map sprite
     * @param mapStyle desired map style
     * @param infoResponse result of InfoRequest
     * @param callback bitmap callback
     */
    public static void getMapSprite(final MapStyle mapStyle, final InfoResponse infoResponse, final MapBitmapCallback callback) {
        final Bitmap mapSprite = mapSpriteCache.get(mapStyle);
        if(mapSprite == null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    final Bitmap mapSprite;
                    try {
                        final URL url = new URL(String.format(MAP_SPRITE_URL, infoResponse.staticContentUrl, mapStyle.getPath()));
                        final URLConnection urlConnection = url.openConnection();
                        urlConnection.setDoInput(true);
                        urlConnection.connect();
                        mapSprite = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    } catch (IOException e) {
                        callback.onError();
                        return null;
                    }
                    mapSpriteCache.put(mapStyle, mapSprite);
                    callback.onBitmapBuilt(mapSprite);
                    return null;
                }
            }.execute();
        } else {
            callback.onBitmapBuilt(mapSprite);
        }
    }

    /**
     * Synchronously draws a layer with place names
     * todo hacky calculation of sizes & positions
     * @param canvas a canvas for the map bitmap to draw layer on
     * @param mapInfo map information
     */
    public static void drawPlaceNamesLayer(final Canvas canvas, final MapResponse mapInfo) {
        if(sizeDenominator > 2) {
            return;
        }

        for(final PlaceInfo placeInfo : mapInfo.places.values()) {
            float textSizeDenominator = sizeDenominator;
            if(PLACE_TEXT_SIZE / sizeDenominator < PLACE_TEXT_SIZE_MIN) {
                textSizeDenominator = PLACE_TEXT_SIZE / PLACE_TEXT_SIZE_MIN;
            }

            final String text = String.format(PLACE_CAPTION, placeInfo.size, placeInfo.name);

            final Paint textPaint = new Paint();
            textPaint.setTextSize(PLACE_TEXT_SIZE / textSizeDenominator);
            textPaint.setColor(TheTaleClientApplication.getContext().getResources().getColor(R.color.map_place_name));

            final Rect textRect = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), textRect);

            final float x = ((placeInfo.x + 0.5f) * MAP_TILE_SIZE + PLACE_TEXT_X_SHIFT) / sizeDenominator - (textRect.right - textRect.left) / 2.0f;
            final float y = ((placeInfo.y + 1.0f) * MAP_TILE_SIZE + PLACE_TEXT_Y_SHIFT) / sizeDenominator;

            final Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(TheTaleClientApplication.getContext().getResources().getColor(R.color.map_place_name_background));

            canvas.drawRect(
                    x + textRect.left - PLACE_TEXT_BACKGROUND_PADDING * 2 * (sizeDenominator / textSizeDenominator),
                    y + textRect.top - PLACE_TEXT_BACKGROUND_PADDING,
                    x + textRect.right + PLACE_TEXT_BACKGROUND_PADDING * 4 * (sizeDenominator / textSizeDenominator),
                    y + textRect.bottom + PLACE_TEXT_BACKGROUND_PADDING,
                    backgroundPaint);
            canvas.drawText(text, 0, text.length(), x, y, textPaint);
        }
    }

    /**
     * Synchronously draws a layer with hero
     * @param canvas a canvas for the map bitmap to draw layer on
     * @param heroInfo hero information
     * @param sprite map sprite
     */
    public static void drawHeroLayer(final Canvas canvas, final HeroInfo heroInfo, final Bitmap sprite) {
        final Rect dst = new Rect(
                (int) (heroInfo.position.x * MAP_TILE_SIZE / sizeDenominator),
                (int) ((heroInfo.position.y * MAP_TILE_SIZE + HERO_SPRITE_SHIFT_Y) / sizeDenominator),
                (int) ((heroInfo.position.x + 1.0) * MAP_TILE_SIZE / sizeDenominator),
                (int) (((heroInfo.position.y + 1.0) * MAP_TILE_SIZE + HERO_SPRITE_SHIFT_Y) / sizeDenominator));
        final SpriteTileInfo tile = getSpriteTile(heroInfo.spriteId);
        if(heroInfo.position.sightX >= 0) {
            final Rect src = new Rect(
                    tile.x, tile.y,
                    tile.x + tile.size, tile.y + tile.size);
            canvas.drawBitmap(sprite, src, dst, null);
        } else {
            final Matrix mirroringMatrix = new Matrix();
            mirroringMatrix.setScale(-1, 1);
            final Bitmap mirroredTileBitmap = Bitmap.createBitmap(
                    sprite,
                    tile.x, tile.y,
                    MAP_TILE_SIZE, MAP_TILE_SIZE,
                    mirroringMatrix, true);
            canvas.drawBitmap(mirroredTileBitmap, new Rect(0, 0, MAP_TILE_SIZE, MAP_TILE_SIZE), dst, null);
        }
    }

    public static void drawModificationLayer(final Canvas canvas, final MapResponse mapInfo,
                                             final MapTerrainResponse terrainInfo, final MapModification modification) {
        MapModification.init(modification, mapInfo);
        for(int y = 0; y < mapInfo.height; y++) {
            final List<MapCellTerrainInfo> row = terrainInfo.cells.get(y);
            for(int x = 0; x < mapInfo.width; x++) {
                modification.modifyCell(canvas, x, y, row.get(x));
            }
        }
    }

    public static SpriteTileInfo getSpriteTile(final int spriteId, final int rotation) {
        return new SpriteTileInfo(MAP_SPRITE_OFFSET_X[spriteId], MAP_SPRITE_OFFSET_Y[spriteId], rotation, MAP_TILE_SIZE);
    }

    public static SpriteTileInfo getSpriteTile(final int spriteId) {
        return getSpriteTile(spriteId, 0);
    }

    public static void cleanup() {
        for(final MapStyle mapStyle : MapStyle.values()) {
            mapSpriteCache.remove(mapStyle);
        }
    }

    public interface MapBitmapCallback {
        void onBitmapBuilt(Bitmap bitmap);
        void onError();
    }

    public static class ExcludeTileInfo {

        public final int x;
        public final int y;
        public final int index;

        public ExcludeTileInfo(final int x, final int y, final int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }

    }

    public static int getCurrentSizeDenominator() {
        return sizeDenominator;
    }

}
