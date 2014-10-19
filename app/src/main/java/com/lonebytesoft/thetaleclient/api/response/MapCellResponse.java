package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellDangerLevel;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellHumidity;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellTemperature;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellTerrain;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellWindDirection;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellWindSpeed;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hamster
 * @since 15.10.2014
 * todo shame on me for parsing html with regexp
 */
public class MapCellResponse {

    private static final int TERRAIN_TEXT_LINES = 4;

    public final List<String> terrainText;
    private final boolean wasParsed;

    public MapCellTerrain terrain = null;
    public PlaceInfo neighbor = null;
    public boolean isWilderness = false;
    public MapCellWindDirection windDirection = null;
    public MapCellWindSpeed windSpeed = null;
    public MapCellTemperature temperature = null;
    public MapCellHumidity humidity = null;
    public MapCellDangerLevel dangerLevel = null;

    public MapCellResponse(final String response, final MapResponse mapInfo) throws JSONException {
        final Pattern terrainFullPattern = Pattern.compile("<div class=\"tab-pane\" id=\"pgf-cell-map\">.*<ul class=\"unstyled\">(.*)</ul>", Pattern.DOTALL);
        final Matcher terrainFullMatcher = terrainFullPattern.matcher(response);
        terrainText = new ArrayList<>();
        if(terrainFullMatcher.find()) {
            final Pattern terrainPartPattern = Pattern.compile("<li>(.*?)</li>", Pattern.DOTALL);
            final Matcher terrainPartMatcher = terrainPartPattern.matcher(terrainFullMatcher.group(1));
            while(terrainPartMatcher.find()) {
                terrainText.add(terrainPartMatcher.group(1).replaceAll("\\s+", " ").trim());
            }
        }

        wasParsed = (mapInfo != null);

        if(wasParsed && (terrainText.size() == TERRAIN_TEXT_LINES)) {
            Pattern pattern;
            Matcher matcher;

            // terrain, neighboring place
            final Map<String, PlaceInfo> neighborhood = new HashMap<>(mapInfo.places.size());
            final String wilderness = TheTaleClientApplication.getContext().getString(R.string.map_wilderness);
            pattern = Pattern.compile("([\\w\\s]+),\\s+([\\w\\s]+)");
            matcher = pattern.matcher(terrainText.get(0));
            if (matcher.find()) {
                terrain = ObjectUtils.getEnumForCode(MapCellTerrain.class, matcher.group(1).toLowerCase());

                final String placeString = matcher.group(2).toLowerCase();
                if (placeString.equals(wilderness)) {
                    neighbor = null;
                    isWilderness = true;
                } else {
                    final Pattern neighborPattern = Pattern.compile("окрестности\\s*(.*)");
                    final Matcher neighborMatcher = neighborPattern.matcher(placeString);
                    if(neighborMatcher.find()) {
                        final String placeName = neighborMatcher.group(1);
                        PlaceInfo place = neighborhood.get(placeName);
                        if(place == null) {
                            double maxSimilarity = -1;
                            for(final PlaceInfo placeInfo : mapInfo.places.values()) {
                                final int length = Math.min(placeName.length(), placeInfo.name.length());
                                boolean fullSimilarity = true;
                                for(int pos = 0; pos < length; pos++) {
                                    if(placeName.charAt(pos) != placeInfo.name.toLowerCase().charAt(pos)) {
                                        final double similarity = (double) pos / length;
                                        if(similarity > maxSimilarity) {
                                            maxSimilarity = similarity;
                                            place = placeInfo;
                                        }
                                        fullSimilarity = false;
                                        break;
                                    }
                                }
                                if(fullSimilarity) {
                                    place = placeInfo;
                                    break;
                                }
                            }
                            neighborhood.put(placeName, place);
                        }
                        neighbor = place;
                    }
                }
            }

            // wind
            pattern = Pattern.compile("дует\\s+([^\\s]*)\\s+(.*)");
            matcher = pattern.matcher(terrainText.get(1));
            if(matcher.find()) {
                windDirection = ObjectUtils.getEnumForCode(MapCellWindDirection.class, matcher.group(1).toLowerCase());
                windSpeed = ObjectUtils.getEnumForCode(MapCellWindSpeed.class, matcher.group(2).toLowerCase());
            }

            // temperature & humidity
            pattern = Pattern.compile("вокруг\\s+(.*)\\s+и\\s+(.*)");
            matcher = pattern.matcher(terrainText.get(2));
            if(matcher.find()) {
                temperature = ObjectUtils.getEnumForCode(MapCellTemperature.class, matcher.group(1).toLowerCase());
                humidity = ObjectUtils.getEnumForCode(MapCellHumidity.class, matcher.group(2).toLowerCase());
            }

            // danger level
            dangerLevel = ObjectUtils.getEnumForCode(MapCellDangerLevel.class, terrainText.get(3).toLowerCase());
        }
    }

    public boolean isParsed() {
        return (wasParsed &&
                (terrain != null) &&
                ((neighbor != null) || isWilderness) &&
                (windDirection != null) && (windSpeed != null) &&
                (temperature != null) && (humidity != null) &&
                (dangerLevel != null));
    }

}
