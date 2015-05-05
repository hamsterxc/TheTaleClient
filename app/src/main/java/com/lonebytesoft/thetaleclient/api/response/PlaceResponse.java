package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.model.CouncilMemberInfo;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class PlaceResponse extends AbstractApiResponse {

    public int id;
    public String name;
    public String description;
    public int x;
    public int y;
    public boolean isFrontier;
    public int newThreshold;
    public int updateTime;
    public double powerBonusPositive;
    public double powerBonusNegative;
    public List<CouncilMemberInfo> council;

    public PlaceResponse(String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(JSONObject data) throws JSONException {
        id = data.getInt("id");
        name = data.getString("name");
        description = data.getString("description");
        x = data.getJSONObject("position").getInt("x");
        y = data.getJSONObject("position").getInt("y");
        isFrontier = data.getBoolean("frontier");
        newThreshold = data.getInt("new_for");
        updateTime = data.getInt("updated_at");
        powerBonusPositive = data.getJSONObject("power").getDouble("positive_bonus");
        powerBonusNegative = data.getJSONObject("power").getDouble("negative_bonus");

        final JSONArray councilJson = data.getJSONArray("persons");
        final int councilCount = councilJson.length();
        council = new ArrayList<>(councilCount);
        for(int i = 0; i < councilCount; i++) {
            council.add(ObjectUtils.getModelFromJson(CouncilMemberInfo.class, councilJson.getJSONObject(i)));
        }
    }

}
