package com.lonebytesoft.thetaleclient.api.response;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hamster
 * @since 15.10.2014
 * todo shame on me for parsing html with regexp
 */
public class MapCellResponse {

    public final List<String> terrain;

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
    }

}
