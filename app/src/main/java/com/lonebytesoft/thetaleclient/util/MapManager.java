package com.lonebytesoft.thetaleclient.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.api.model.PositionInfo;
import com.lonebytesoft.thetaleclient.api.model.SpriteTileInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.request.MapRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
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

    private static final int STALE_TIMEOUT_MILLIS = 600000; // 10 min

    private static Map<MapStyle, CachedMap> mapCache = new HashMap<>(MapStyle.values().length);
    private static Map<MapStyle, Bitmap> mapSpriteCache = new HashMap<>(MapStyle.values().length);

    public static void buildMapBitmap(final MapBitmapCallback callback, final MapStyle mapStyle) {
        final long time = System.currentTimeMillis();
        final CachedMap cachedMap = mapCache.get(mapStyle);
        if((cachedMap != null) && (cachedMap.map.get() != null) && (cachedMap.time + STALE_TIMEOUT_MILLIS >= time)) {
            callback.onBitmapBuilt(cachedMap.map.get().copy(Bitmap.Config.ARGB_8888, true));
        } else {
            if(cachedMap != null) {
                cachedMap.map.clear();
            }
            mapCache.remove(mapStyle);
            System.gc();

            new InfoRequest().execute(new ApiResponseCallback<InfoResponse>() {
                @Override
                public void processResponse(InfoResponse response) {
                    final String mapSpriteUrl = String.format(MAP_SPRITE_URL, response.staticContentUrl, mapStyle.getPath());

                    new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                        @Override
                        public void processResponse(GameInfoResponse response) {
                            new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                                @Override
                                public void processResponse(final MapResponse response) {
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            Bitmap mapSprite = mapSpriteCache.get(mapStyle);
                                            if(mapSprite == null) {
                                                try {
                                                    final URL url = new URL(mapSpriteUrl);
                                                    final URLConnection urlConnection = url.openConnection();
                                                    urlConnection.setDoInput(true);
                                                    urlConnection.connect();
                                                    mapSprite = BitmapFactory.decodeStream(urlConnection.getInputStream());
                                                } catch (IOException e) {
                                                    callback.onError();
                                                    return null;
                                                }
                                                mapSpriteCache.put(mapStyle, mapSprite);
                                            }

                                            final Bitmap map = Bitmap.createBitmap(
                                                    response.width * MAP_TILE_SIZE,
                                                    response.height * MAP_TILE_SIZE,
                                                    Bitmap.Config.ARGB_8888);
                                            final Canvas mapCanvas = new Canvas(map);

                                            final Rect tileRect = new Rect(0, 0, MAP_TILE_SIZE, MAP_TILE_SIZE);
                                            final int rowsCount = response.tiles.size();
                                            for(int i = 0; i < rowsCount; i++) {
                                                final List<List<SpriteTileInfo>> row = response.tiles.get(i);
                                                final int cellsCount = row.size();
                                                for(int j = 0; j < cellsCount; j++) {
                                                    final Rect dst = new Rect(
                                                            j * MAP_TILE_SIZE, i * MAP_TILE_SIZE,
                                                            (j + 1) * MAP_TILE_SIZE, (i + 1) * MAP_TILE_SIZE);
                                                    for(final SpriteTileInfo tile : row.get(j)) {
                                                        if(tile.rotation == 0) {
                                                            final Rect src = new Rect(
                                                                    tile.x, tile.y,
                                                                    tile.x + tile.size, tile.y + tile.size);
                                                            mapCanvas.drawBitmap(mapSprite, src, dst, null);
                                                        } else {
                                                            final Matrix rotationMatrix = new Matrix();
                                                            rotationMatrix.setRotate(tile.rotation);
                                                            final Bitmap rotatingTileBitmap = Bitmap.createBitmap(
                                                                    mapSprite,
                                                                    tile.x, tile.y,
                                                                    MAP_TILE_SIZE, MAP_TILE_SIZE,
                                                                    rotationMatrix, true);
                                                            mapCanvas.drawBitmap(rotatingTileBitmap, tileRect, dst, null);
                                                        }
                                                    }
                                                }
                                            }

                                            // todo hacky calculation of sizes & positions
                                            for(final Map.Entry<Integer, PlaceInfo> entry : response.places.entrySet()) {
                                                final PlaceInfo placeInfo = entry.getValue();
                                                final String text = String.format(PLACE_CAPTION, placeInfo.size, placeInfo.name);

                                                final Paint textPaint = new Paint();
                                                textPaint.setTextSize(PLACE_TEXT_SIZE);
                                                textPaint.setColor(TheTaleClientApplication.getContext().getResources().getColor(R.color.map_place_name));

                                                final Rect textRect = new Rect();
                                                textPaint.getTextBounds(text, 0, text.length(), textRect);

                                                final float x = (placeInfo.x + 0.5f) * MAP_TILE_SIZE - (textRect.right - textRect.left) / 2.0f + PLACE_TEXT_X_SHIFT;
                                                final float y = (placeInfo.y + 1.0f) * MAP_TILE_SIZE + PLACE_TEXT_Y_SHIFT;

                                                final Paint backgroundPaint = new Paint();
                                                backgroundPaint.setColor(TheTaleClientApplication.getContext().getResources().getColor(R.color.map_place_name_background));

                                                mapCanvas.drawRect(
                                                        x + textRect.left - PLACE_TEXT_BACKGROUND_PADDING * 2,
                                                        y + textRect.top - PLACE_TEXT_BACKGROUND_PADDING,
                                                        x + textRect.right + PLACE_TEXT_BACKGROUND_PADDING * 4,
                                                        y + textRect.bottom + PLACE_TEXT_BACKGROUND_PADDING,
                                                        backgroundPaint);
                                                mapCanvas.drawText(text, 0, text.length(), x, y, textPaint);
                                            }

                                            final CachedMap built = new CachedMap();
                                            built.time = time;
                                            built.map = new WeakReference<>(map);
                                            mapCache.put(mapStyle, built);

                                            callback.onBitmapBuilt(map.copy(Bitmap.Config.ARGB_8888, true));

                                            return null;
                                        }
                                    }.execute();
                                }

                                @Override
                                public void processError(String error) {
                                    callback.onError();
                                }
                            });
                        }

                        @Override
                        public void processError(GameInfoResponse response) {
                            callback.onError();
                        }
                    });
                }

                @Override
                public void processError(InfoResponse response) {
                    callback.onError();
                }
            });
        }
    }

    public static void drawHero(final Bitmap map, final PositionInfo position, final SpriteTileInfo tile, final MapStyle mapStyle) {
        final Bitmap mapSprite = mapSpriteCache.get(mapStyle);
        if(mapSprite == null) {
            throw new IllegalStateException("buildMapBitmap() must be called before drawHero()");
        }

        final Canvas mapCanvas = new Canvas(map);
        final Rect dst = new Rect(
                (int) (position.x * MAP_TILE_SIZE), (int) (position.y * MAP_TILE_SIZE + HERO_SPRITE_SHIFT_Y),
                (int) ((position.x + 1.0) * MAP_TILE_SIZE), (int) ((position.y + 1.0) * MAP_TILE_SIZE + HERO_SPRITE_SHIFT_Y));
        if(position.sightX >= 0) {
            final Rect src = new Rect(
                    tile.x, tile.y,
                    tile.x + tile.size, tile.y + tile.size);
            mapCanvas.drawBitmap(mapSprite, src, dst, null);
        } else {
            final Matrix mirroringMatrix = new Matrix();
            mirroringMatrix.setScale(-1, 1);
            final Bitmap mirroredTileBitmap = Bitmap.createBitmap(
                    mapSprite,
                    tile.x, tile.y,
                    MAP_TILE_SIZE, MAP_TILE_SIZE,
                    mirroringMatrix, true);
            mapCanvas.drawBitmap(mirroredTileBitmap, new Rect(0, 0, MAP_TILE_SIZE, MAP_TILE_SIZE), dst, null);
        }
    }

    public static SpriteTileInfo getSpriteTile(final int spriteId, final int rotation) {
        return new SpriteTileInfo(MAP_SPRITE_OFFSET_X[spriteId], MAP_SPRITE_OFFSET_Y[spriteId], rotation, MAP_TILE_SIZE);
    }

    public static SpriteTileInfo getSpriteTile(final int spriteId) {
        return getSpriteTile(spriteId, 0);
    }

    public interface MapBitmapCallback {
        void onBitmapBuilt(Bitmap map);
        void onError();
    }

    private static class CachedMap {
        public long time;
        public WeakReference<Bitmap> map;
    }

    public static void cleanup() {
        for(final MapStyle mapStyle : MapStyle.values()) {
            final CachedMap cachedMap = mapCache.get(mapStyle);
            if ((cachedMap != null) && (cachedMap.map != null)) {
                cachedMap.map.clear();
            }
            mapCache.remove(mapStyle);

            mapSpriteCache.remove(mapStyle);
        }
    }

}
