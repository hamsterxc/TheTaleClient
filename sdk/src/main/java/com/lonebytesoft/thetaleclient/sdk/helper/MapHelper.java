package com.lonebytesoft.thetaleclient.sdk.helper;

import com.lonebytesoft.thetaleclient.sdk.Urls;
import com.lonebytesoft.thetaleclient.sdk.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.model.HeroInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MapPlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MapTileInfo;
import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.InfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.MapRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public class MapHelper {

    private static final int SPRITE_TILE_SIZE = 32;
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

    private static final Color PLACE_TEXT_BACKGROUND_COLOR = new Color(0, 0, 0, 192); // #c0000000
    private static final Color PLACE_TEXT_COLOR = Color.WHITE;
    private static final int PLACE_TEXT_PADDING_X = 4;
    private static final int PLACE_TEXT_PADDING_Y = 2;
    private static final int PLACE_TEXT_SHIFT_X = 0;
    private static final int PLACE_TEXT_SHIFT_Y = -2;

    private static final int HERO_SHIFT_X = 0;
    private static final int HERO_SHIFT_Y = -12;

    private static final Map<MapStyle, BufferedImage> sprites = new HashMap<>();

    public static BufferedImage getMapImage(
            final int width, final int height, final MapStyle style, final boolean shouldDrawPlaceNames,
            final MapResponse mapInfo, final String staticContentUrl,
            final List<HeroInfo> heroes, final boolean conserveSpace) {
        final int imageType = conserveSpace ? BufferedImage.TYPE_USHORT_565_RGB : BufferedImage.TYPE_INT_ARGB;
        final BufferedImage map = new BufferedImage(width, height, imageType);
        final Graphics mapGraphics = map.createGraphics();

        BufferedImage sprite = sprites.get(style);
        if(sprite == null) {
            try {
                sprite = ImageIO.read(new URL(String.format("%s%s%s", Urls.BASE_PROTOCOL, staticContentUrl, style.path)));
                sprites.put(style, sprite);
            } catch (IOException e) {
                return null;
            }
        }

        final double tileWidth = (double) width / mapInfo.width;
        final double tileHeight = (double) height / mapInfo.height;

        // base layer
        for(int y = 0; y < mapInfo.height; y++) {
            final List<List<MapTileInfo>> rowTiles = mapInfo.tiles.get(y);
            for(int x = 0; x < mapInfo.width; x++) {
                for(final MapTileInfo tileInfo : rowTiles.get(x)) {
                    drawSpriteImage(
                            mapGraphics,
                            (int) Math.round(x * tileWidth), (int) Math.round(y * tileHeight),
                            (int) Math.ceil(tileWidth), (int) Math.ceil(tileHeight),
                            sprite, tileInfo.spriteTileId, tileInfo.rotation);
                }
            }
        }

        // place names
        if(shouldDrawPlaceNames) {
            final Font font = mapGraphics.getFont();
            mapGraphics.setFont(font.deriveFont((float) (font.getSize() * (Math.min(tileWidth, tileHeight) / SPRITE_TILE_SIZE))));
            final double placeTextPaddingX = (double) PLACE_TEXT_PADDING_X * tileWidth / SPRITE_TILE_SIZE;
            final double placeTextPaddingY = (double) PLACE_TEXT_PADDING_Y * tileHeight / SPRITE_TILE_SIZE;
            final double placeTextShiftX = (double) PLACE_TEXT_SHIFT_X * tileWidth / SPRITE_TILE_SIZE;
            final double placeTextShiftY = (double) PLACE_TEXT_SHIFT_Y * tileHeight / SPRITE_TILE_SIZE;
            for(final MapPlaceInfo mapPlaceInfo : mapInfo.places.values()) {
                final String title = String.format("%s (%d)", mapPlaceInfo.name, mapPlaceInfo.size);
                final Rectangle2D rect = mapGraphics.getFontMetrics().getStringBounds(title, mapGraphics);

                final double x = (mapPlaceInfo.x + 0.5) * tileWidth - rect.getWidth() / 2.0;
                final double y = (mapPlaceInfo.y + 1.5) * tileHeight;

                mapGraphics.setColor(PLACE_TEXT_BACKGROUND_COLOR);
                mapGraphics.fillRect(
                        (int) (x + rect.getMinX() - placeTextPaddingX),
                        (int) (y + rect.getMinY() - placeTextPaddingY),
                        (int) (rect.getWidth() + placeTextPaddingX * 2),
                        (int) (rect.getHeight() + placeTextPaddingY * 2));

                mapGraphics.setColor(PLACE_TEXT_COLOR);
                mapGraphics.drawString(title, (int) (x + placeTextShiftX), (int) (y + placeTextShiftY));
            }
        }

        // heroes
        if(heroes != null) {
            final double heroShiftX = (double) HERO_SHIFT_X * tileWidth / SPRITE_TILE_SIZE;
            final double heroShiftY = (double) HERO_SHIFT_Y * tileHeight / SPRITE_TILE_SIZE;
            for(final HeroInfo heroInfo : heroes) {
                drawSpriteImage(
                        mapGraphics,
                        (int) Math.round(heroInfo.position.x * tileWidth + heroShiftX),
                        (int) Math.round(heroInfo.position.y * tileHeight + heroShiftY),
                        (int) Math.ceil(tileWidth), (int) Math.ceil(tileHeight),
                        sprite, heroInfo.spriteId, heroInfo.position.sightX < 0);
            }
        }

        return map;
    }

    public static BufferedImage getMapImage(final String clientId, final MapStyle style) {
        try {
            final GameInfoResponse gameInfoResponse = new GameInfoRequest(clientId).execute();
            final InfoResponse infoResponse = new InfoRequest(clientId).execute();
            final MapResponse mapResponse = new MapRequest(
                    infoResponse.dynamicContentUrl, gameInfoResponse.mapVersion).execute();

            final List<HeroInfo> heroes;
            if(gameInfoResponse.account == null) {
                heroes = null;
            } else {
                heroes = new ArrayList<>(1);
                heroes.add(gameInfoResponse.account.hero);
            }

            final Point size = getMapSize(mapResponse);
            return getMapImage(
                    size.x, size.y, style,
                    true, mapResponse, infoResponse.staticContentUrl, heroes, false);
        } catch (ApiException e) {
            return null;
        }
    }

    public static Point getMapSize(final MapResponse mapInfo) {
        return new Point(mapInfo.width * SPRITE_TILE_SIZE, mapInfo.height * SPRITE_TILE_SIZE);
    }

    private static void drawSpriteImage(final Graphics target,
                                        final int x, final int y, final int width, final int height,
                                        final Image sprite, final int spriteImageId, final int rotation) {
        final double angle = Math.toRadians(rotation);

        final BufferedImage tileImage = new BufferedImage(SPRITE_TILE_SIZE, SPRITE_TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tileGraphics = tileImage.createGraphics();
        if(rotation != 0) {
            tileGraphics.rotate(angle, SPRITE_TILE_SIZE / 2.0, SPRITE_TILE_SIZE / 2.0);
        }
        tileGraphics.drawImage(sprite,
                0, 0, SPRITE_TILE_SIZE, SPRITE_TILE_SIZE,
                SPRITE_TILE_OFFSET_X[spriteImageId], SPRITE_TILE_OFFSET_Y[spriteImageId],
                SPRITE_TILE_OFFSET_X[spriteImageId] + SPRITE_TILE_SIZE, SPRITE_TILE_OFFSET_Y[spriteImageId] + SPRITE_TILE_SIZE,
                null);

        target.drawImage(tileImage,
                x, y, x + width, y + height,
                0, 0, SPRITE_TILE_SIZE, SPRITE_TILE_SIZE,
                null);
    }

    private static void drawSpriteImage(final Graphics target,
                                        final int x, final int y, final int width, final int height,
                                        final Image sprite, final int spriteImageId, final boolean isMirrored) {
        BufferedImage tileImage = new BufferedImage(SPRITE_TILE_SIZE, SPRITE_TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tileGraphics = tileImage.createGraphics();
        tileGraphics.drawImage(sprite,
                0, 0, SPRITE_TILE_SIZE, SPRITE_TILE_SIZE,
                SPRITE_TILE_OFFSET_X[spriteImageId], SPRITE_TILE_OFFSET_Y[spriteImageId],
                SPRITE_TILE_OFFSET_X[spriteImageId] + SPRITE_TILE_SIZE, SPRITE_TILE_OFFSET_Y[spriteImageId] + SPRITE_TILE_SIZE,
                null);

        if(isMirrored) {
            final AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
            affineTransform.translate(-SPRITE_TILE_SIZE, 0);
            tileImage = new AffineTransformOp(affineTransform, null).filter(tileImage, null);
        }

        target.drawImage(tileImage,
                x, y, x + width, y + height,
                0, 0, SPRITE_TILE_SIZE, SPRITE_TILE_SIZE,
                null);
    }

}
