package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.RoadDirection;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 12.04.2015
 */
public class RoadInfo {

    public final int id;
    public final int startPlaceId;
    public final int endPlaceId;
    public final boolean isExist;
    public final double length;
    public final List<RoadDirection> path;

    public RoadInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        startPlaceId = json.getInt("point_1_id");
        endPlaceId = json.getInt("point_2_id");
        isExist = json.getBoolean("exists");
        length = json.getDouble("length");

        final String pathString = json.getString("path");
        final int pathLength = pathString.length();
        path = new ArrayList<>(pathLength);
        for(int i = 0; i < pathLength; i++) {
            path.add(ObjectUtils.getEnumForCode(RoadDirection.class, pathString.charAt(i)));
        }
    }

}
