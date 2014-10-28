package com.lonebytesoft.thetaleclient.api.response;

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

    public final List<String> terrain;
    public final Map<String, Integer> influence;

    public MapCellResponse(final String response) throws JSONException {
        final Pattern terrainFullPattern = Pattern.compile("<div class=\"tab-pane\" id=\"pgf-cell-map\">.*<ul class=\"unstyled\">(.*)</ul>", Pattern.DOTALL);
        final Matcher terrainFullMatcher = terrainFullPattern.matcher(response);
        terrain = new ArrayList<>();
        if(terrainFullMatcher.find()) {
            final Pattern terrainPartPattern = Pattern.compile("<li>(.*?)</li>", Pattern.DOTALL);
            final Matcher terrainPartMatcher = terrainPartPattern.matcher(terrainFullMatcher.group(1));
            while(terrainPartMatcher.find()) {
                terrain.add(terrainPartMatcher.group(1).replaceAll("\\s+", " ").trim());
            }
        }

        final Pattern influencePattern = Pattern.compile("<i class=\"profession-icon.*?<th>\\s*(\\w*).*?<td[^>]*[^\\d]*(\\d*)", Pattern.DOTALL);
        final Matcher influenceMatcher = influencePattern.matcher(response);
        influence = new HashMap<>();
        while(influenceMatcher.find()) {
            influence.put(influenceMatcher.group(1), Integer.decode(influenceMatcher.group(2)));
        }
    }

}
