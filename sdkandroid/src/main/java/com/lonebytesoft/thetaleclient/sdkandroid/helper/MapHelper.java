package com.lonebytesoft.thetaleclient.sdkandroid.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;

import com.lonebytesoft.thetaleclient.sdk.Urls;
import com.lonebytesoft.thetaleclient.sdk.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.sdk.model.HeroInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MapPlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MapTileInfo;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.util.SystemUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is copied from package com.lonebytesoft.thetaleclient.sdk.helper
 * with changes to work in Android
 * @author Hamster
 * @since 28.04.2015
 */
public class MapHelper {

    private static final int SPRITE_TILE_SIZE = 32;
    private static final Rect SPRITE_TILE_RECT = new Rect(0, 0, SPRITE_TILE_SIZE, SPRITE_TILE_SIZE);
    private static final int[] SPRITE_TILE_OFFSET_X = new int[]{
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
    private static final int[] SPRITE_TILE_OFFSET_Y = new int[]{
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

    private static final int PLACE_TEXT_BACKGROUND_COLOR = Color.argb(192, 0, 0, 0); // #c0000000
    private static final int PLACE_TEXT_COLOR = Color.WHITE;
    private static final int PLACE_TEXT_PADDING_X = 2;
    private static final int PLACE_TEXT_PADDING_Y = 2;
    private static final int PLACE_TEXT_SHIFT_X = -2;
    private static final int PLACE_TEXT_SHIFT_Y = 12;

    private static final int HERO_SHIFT_X = 0;
    private static final int HERO_SHIFT_Y = -12;

    private static final double MEMORY_PART_MAX = 0.8;

    private static final Map<MapStyle, Bitmap> sprites = new HashMap<>();

    public static Bitmap getMapImage(
            final int width, final int height, final MapStyle style, final boolean shouldDrawPlaceNames,
            final MapResponse mapInfo, final String staticContentUrl,
            final List<HeroInfo> heroes, final boolean conserveSpace) {
        final Bitmap.Config config = conserveSpace ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
        final Bitmap map = Bitmap.createBitmap(width, height, config);
        final Canvas mapCanvas = new Canvas(map);

        Bitmap sprite = sprites.get(style);
        if(sprite == null) {
            try {
                final URL url = new URL(String.format("%s%s%s", Urls.BASE_PROTOCOL, staticContentUrl, style.path));
                final URLConnection urlConnection = url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.connect();
                sprite = BitmapFactory.decodeStream(urlConnection.getInputStream());
            } catch (IOException e) {
                return null;
            }
            sprites.put(style, sprite);
        }

        final double tileWidth = (double) width / mapInfo.width;
        final double tileHeight = (double) height / mapInfo.height;

        // base layer
        for(int y = 0; y < mapInfo.height; y++) {
            final List<List<MapTileInfo>> rowTiles = mapInfo.tiles.get(y);
            for(int x = 0; x < mapInfo.width; x++) {
                for(final MapTileInfo tileInfo : rowTiles.get(x)) {
                    drawSpriteImage(
                            mapCanvas,
                            (int) Math.round(x * tileWidth), (int) Math.round(y * tileHeight),
                            (int) Math.ceil(tileWidth), (int) Math.ceil(tileHeight),
                            sprite, tileInfo.spriteTileId, tileInfo.rotation);
                }
            }
        }

        // place names
        // TODO hacky calculation of sizes & positions
        if(shouldDrawPlaceNames) {
            final Paint textBackgroundPaint = new Paint();
            textBackgroundPaint.setColor(PLACE_TEXT_BACKGROUND_COLOR);
            final Paint textPaint = new Paint();
            textPaint.setTextSize((float) (textPaint.getTextSize() * (Math.min(tileWidth, tileHeight) / SPRITE_TILE_SIZE)));
            textPaint.setColor(PLACE_TEXT_COLOR);
            final Rect textRect = new Rect();

            final double placeTextPaddingX = (double) PLACE_TEXT_PADDING_X * tileWidth / SPRITE_TILE_SIZE;
            final double placeTextPaddingY = (double) PLACE_TEXT_PADDING_Y * tileHeight / SPRITE_TILE_SIZE;
            final double placeTextShiftX = (double) PLACE_TEXT_SHIFT_X * tileWidth / SPRITE_TILE_SIZE;
            final double placeTextShiftY = (double) PLACE_TEXT_SHIFT_Y * tileHeight / SPRITE_TILE_SIZE;

            for(final MapPlaceInfo mapPlaceInfo : mapInfo.places.values()) {
                final String title = String.format("%s (%d)", mapPlaceInfo.name, mapPlaceInfo.size);
                textPaint.getTextBounds(title, 0, title.length(), textRect);

                final double x = (mapPlaceInfo.x + 0.5) * tileWidth + placeTextShiftX - textRect.width() / 2.0;
                final double y = (mapPlaceInfo.y + 1.0) * tileHeight + placeTextShiftY;

                mapCanvas.drawRect(
                        (float) (x + textRect.left - placeTextPaddingX * 2),
                        (float) (y + textRect.top - placeTextPaddingY),
                        (float) (x + textRect.right + placeTextPaddingX * 4),
                        (float) (y + textRect.bottom + placeTextPaddingY),
                        textBackgroundPaint);

                mapCanvas.drawText(title, (float) x, (float) y, textPaint);
            }
        }

        // heroes
        if(heroes != null) {
            final double heroShiftX = (double) HERO_SHIFT_X * tileWidth / SPRITE_TILE_SIZE;
            final double heroShiftY = (double) HERO_SHIFT_Y * tileHeight / SPRITE_TILE_SIZE;
            for(final HeroInfo heroInfo : heroes) {
                drawSpriteImage(
                        mapCanvas,
                        (int) Math.round(heroInfo.position.x * tileWidth + heroShiftX),
                        (int) Math.round(heroInfo.position.y * tileHeight + heroShiftY),
                        (int) Math.ceil(tileWidth), (int) Math.ceil(tileHeight),
                        sprite, heroInfo.spriteId, heroInfo.position.sightX < 0);
            }
        }

        return map;
    }

    public static Point getMapSize(final MapResponse mapInfo) {
        return new Point(mapInfo.width * SPRITE_TILE_SIZE, mapInfo.height * SPRITE_TILE_SIZE);
    }

    public static MapMemorySettings getAvailableMapMemorySettings(final MapResponse mapInfo) {
        final Point mapSize = getMapSize(mapInfo);
        for(int sizeDenominator = 1;; sizeDenominator++) {
            final long size = (long) (((double) mapSize.x / sizeDenominator) * ((double) mapSize.y / sizeDenominator) * 4.0);
            if(size < SystemUtils.getFreeMemory() * MEMORY_PART_MAX) {
                return new MapMemorySettings(
                        (int) Math.floor(mapSize.x / sizeDenominator),
                        (int) Math.floor(mapSize.y / sizeDenominator),
                        (sizeDenominator > 1) || (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH));
            }
        }
    }

    private static void drawSpriteImage(final Canvas target,
                                        final int x, final int y, final int width, final int height,
                                        final Bitmap sprite, final int spriteImageId, final int rotation) {
        if(rotation == 0) {
            drawSpriteImage(target, x, y, width, height, sprite, spriteImageId);
        } else {
            final Matrix rotationMatrix = new Matrix();
            rotationMatrix.setRotate(rotation);
            drawSpriteImage(target, x, y, width, height, sprite, spriteImageId, rotationMatrix);
        }
    }

    private static void drawSpriteImage(final Canvas target,
                                        final int x, final int y, final int width, final int height,
                                        final Bitmap sprite, final int spriteImageId, final boolean isMirrored) {
        if(isMirrored) {
            final Matrix mirroringMatrix = new Matrix();
            mirroringMatrix.setScale(-1, 1);
            drawSpriteImage(target, x, y, width, height, sprite, spriteImageId, mirroringMatrix);
        } else {
            drawSpriteImage(target, x, y, width, height, sprite, spriteImageId);
        }
    }

    private static void drawSpriteImage(final Canvas target,
                                        final int x, final int y, final int width, final int height,
                                        final Bitmap sprite, final int spriteImageId, final Matrix matrix) {
        final Bitmap tileBitmap = Bitmap.createBitmap(
                sprite,
                SPRITE_TILE_OFFSET_X[spriteImageId], SPRITE_TILE_OFFSET_Y[spriteImageId],
                SPRITE_TILE_SIZE, SPRITE_TILE_SIZE,
                matrix, true);
        target.drawBitmap(
                tileBitmap,
                SPRITE_TILE_RECT,
                new Rect(x, y, x + width, y + height),
                null);
    }

    private static void drawSpriteImage(final Canvas target,
                                        final int x, final int y, final int width, final int height,
                                        final Bitmap sprite, final int spriteImageId) {
        target.drawBitmap(
                sprite,
                new Rect(
                        SPRITE_TILE_OFFSET_X[spriteImageId],
                        SPRITE_TILE_OFFSET_Y[spriteImageId],
                        SPRITE_TILE_OFFSET_X[spriteImageId] + SPRITE_TILE_SIZE,
                        SPRITE_TILE_OFFSET_Y[spriteImageId] + SPRITE_TILE_SIZE),
                new Rect(x, y, x + width, y + height),
                null);
    }

    public static class MapMemorySettings {
        public final int width;
        public final int height;
        public final boolean conserveSpace;

        public MapMemorySettings(final int width, final int height, final boolean conserveSpace) {
            this.width = width;
            this.height = height;
            this.conserveSpace = conserveSpace;
        }
    }

}
