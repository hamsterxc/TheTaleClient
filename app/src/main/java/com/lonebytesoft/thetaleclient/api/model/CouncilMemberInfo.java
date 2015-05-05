package com.lonebytesoft.thetaleclient.api.model;

import com.lonebytesoft.thetaleclient.api.dictionary.Gender;
import com.lonebytesoft.thetaleclient.api.dictionary.Profession;
import com.lonebytesoft.thetaleclient.api.dictionary.Race;
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
public class CouncilMemberInfo {

    public final int id;
    public final String name;
    public final Gender gender;
    public final Race race;
    public final Profession profession;
    public final int newThreshold;
    public final Integer buildingId;
    public final double mastery;
    public final String masteryVerbose;
    public final CouncilMemberPowerInfo power;
    public final List<Integer> friends;
    public final List<Integer> enemies;
    public final List<CouncilMemberConnectionInfo> connections;

    public CouncilMemberInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        gender = ObjectUtils.getEnumForCode(Gender.class, json.getInt("gender"));
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        profession = ObjectUtils.getEnumForCode(Profession.class, json.getInt("type"));
        newThreshold = json.getInt("unfreeze_in");
        buildingId = ObjectUtils.getOptionalInteger(json, "building");
        mastery = json.getJSONObject("mastery").getDouble("value");
        masteryVerbose = json.getJSONObject("mastery").getString("name");
        power = ObjectUtils.getModelFromJson(CouncilMemberPowerInfo.class, json.getJSONObject("power"));

        final JSONArray friendsJson = json.getJSONObject("keepers").getJSONArray("friends");
        final int friendsCount = friendsJson.length();
        friends = new ArrayList<>(friendsCount);
        for(int i = 0; i < friendsCount; i++) {
            friends.add(friendsJson.getInt(i));
        }

        final JSONArray enemiesJson = json.getJSONObject("keepers").getJSONArray("enemies");
        final int enemiesCount = enemiesJson.length();
        enemies = new ArrayList<>(enemiesCount);
        for(int i = 0; i < enemiesCount; i++) {
            enemies.add(enemiesJson.getInt(i));
        }

        final JSONArray connectionsJson = json.getJSONArray("connections");
        final int connectionsCount = connectionsJson.length();
        connections = new ArrayList<>(connectionsCount);
        for(int i = 0; i < connectionsCount; i++) {
            connections.add(ObjectUtils.getModelFromJson(CouncilMemberConnectionInfo.class,
                    ObjectUtils.getObjectFromArray(connectionsJson.getJSONArray(i),
                            new String[]{"social_link", "council_member"})));
        }
    }

}
