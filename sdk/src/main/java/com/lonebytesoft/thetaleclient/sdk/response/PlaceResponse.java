package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Habit;
import com.lonebytesoft.thetaleclient.sdk.dictionary.PlaceParameter;
import com.lonebytesoft.thetaleclient.sdk.dictionary.PlaceSpecialization;
import com.lonebytesoft.thetaleclient.sdk.model.BillInfo;
import com.lonebytesoft.thetaleclient.sdk.model.CouncilMemberInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceAccountInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceChronicleEntry;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceClanInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceHabitInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceParameterInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceRaceInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceSpecializationInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 06.05.2015
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
    public List<Integer> friends;
    public List<Integer> enemies;
    public Map<PlaceParameter, PlaceParameterInfo> parameters;
    public List<PlaceRaceInfo> demographics;
    public List<BillInfo> bills;
    public PlaceSpecialization specializationCurrent;
    public List<PlaceSpecializationInfo> specializations;
    public Map<Habit, PlaceHabitInfo> habits;
    public List<PlaceChronicleEntry> chronicle;
    public Map<Integer, PlaceAccountInfo> accounts;
    public Map<Integer, PlaceClanInfo> clans;

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

        council = ObjectUtils.getModelListFromJson(CouncilMemberInfo.class, data.getJSONArray("persons"));

        final JSONObject keepersJson = data.getJSONObject("keepers");
        final JSONArray friendsJson = keepersJson.getJSONArray("friends");
        final int friendsCount = friendsJson.length();
        friends = new ArrayList<>(friendsCount);
        for(int i = 0; i < friendsCount; i++) {
            friends.add(friendsJson.getInt(i));
        }

        final JSONArray enemiesJson = keepersJson.getJSONArray("enemies");
        final int enemiesCount = enemiesJson.length();
        enemies = new ArrayList<>(enemiesCount);
        for(int i = 0; i < enemiesCount; i++) {
            enemies.add(enemiesJson.getInt(i));
        }

        parameters = new HashMap<>(PlaceParameter.values().length);
        final JSONObject parametersJson = data.getJSONObject("parameters");
        for(final PlaceParameter placeParameter : PlaceParameter.values()) {
            parameters.put(placeParameter, ObjectUtils.getModelFromJson(PlaceParameterInfo.class,
                    parametersJson.getJSONObject(placeParameter.code)));
        }

        demographics = ObjectUtils.getModelListFromJson(PlaceRaceInfo.class, data.getJSONArray("demographics"));

        bills = ObjectUtils.getModelListFromJson(BillInfo.class, data.getJSONArray("bills"));

        final JSONObject specializationsJson = data.getJSONObject("specializations");
        final Integer specializationCode = ObjectUtils.getOptionalInteger(specializationsJson, "current");
        specializationCurrent = specializationCode == null
                ? null
                : ObjectUtils.getEnumForCode(PlaceSpecialization.class, specializationCode);
        specializations = ObjectUtils.getModelListFromJson(PlaceSpecializationInfo.class,
                specializationsJson.getJSONArray("all"));

        habits = new HashMap<>(Habit.values().length);
        final JSONObject habitsJson = data.getJSONObject("habits");
        for(final Habit habit : Habit.values()) {
            habits.put(habit, ObjectUtils.getModelFromJson(PlaceHabitInfo.class,
                    habitsJson.getJSONObject(String.valueOf(habit.code))));
        }

        chronicle = ObjectUtils.getModelListFromJson(PlaceChronicleEntry.class,
                data.getJSONArray("chronicle"), new String[]{"date_short", "date", "text"});

        final JSONObject accountsJson = data.getJSONObject("accounts");
        accounts = new HashMap<>(accountsJson.length());
        for(final Iterator<String> accountsJsonIterator = accountsJson.keys(); accountsJsonIterator.hasNext();) {
            final String key = accountsJsonIterator.next();
            accounts.put(Integer.decode(key),
                    ObjectUtils.getModelFromJson(PlaceAccountInfo.class, accountsJson.getJSONObject(key)));
        }

        final JSONObject clansJson = data.getJSONObject("clans");
        clans = new HashMap<>(clansJson.length());
        for(final Iterator<String> clansJsonIterator = clansJson.keys(); clansJsonIterator.hasNext();) {
            final String key = clansJsonIterator.next();
            clans.put(Integer.decode(key),
                    ObjectUtils.getModelFromJson(PlaceClanInfo.class, clansJson.getJSONObject(key)));
        }
    }

}
