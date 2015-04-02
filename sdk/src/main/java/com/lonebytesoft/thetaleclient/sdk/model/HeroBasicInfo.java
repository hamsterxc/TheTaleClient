package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Gender;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class HeroBasicInfo {

    public final String name;
    public final Race race;
    public final Gender gender;
    public final int destinyPoints;
    public final boolean isAlive;

    public final int level;
    public final int experienceCurrent;
    public final int experienceForNextLevel;

    public final int healthCurrent;
    public final int healthMax;

    public final int powerPhysical;
    public final int powerMagical;

    public final double moveSpeed;
    public final double initiative;

    public final int money;
    public final int bagCapacity;
    public final int bagItemsCount;

    public HeroBasicInfo(final JSONObject json) throws JSONException {
        name = json.getString("name");
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        gender = ObjectUtils.getEnumForCode(Gender.class, json.getInt("gender"));
        destinyPoints = json.getInt("destiny_points");
        isAlive = json.getBoolean("alive");

        level = json.getInt("level");
        experienceCurrent = json.getInt("experience");
        experienceForNextLevel = json.getInt("experience_to_level");

        healthCurrent = json.getInt("health");
        healthMax = json.getInt("max_health");

        powerPhysical = json.getJSONArray("power").getInt(0);
        powerMagical = json.getJSONArray("power").getInt(1);

        moveSpeed = json.getDouble("move_speed");
        initiative = json.getDouble("initiative");

        money = json.getInt("money");
        bagCapacity = json.getInt("max_bag_size");
        bagItemsCount = json.getInt("loot_items_count");
    }

}
