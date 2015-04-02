package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.CompanionSpecies;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class CompanionInfo {

    public final CompanionSpecies species;
    public final String name;

    public final int healthCurrent;
    public final int healthMax;

    public final int coherence;
    public final int coherenceReal;
    public final int experienceCurrent;
    public final int experienceForNextLevel;

    public CompanionInfo(final JSONObject json) throws JSONException {
        species = ObjectUtils.getEnumForCode(CompanionSpecies.class, json.getInt("type"));
        name = json.getString("name");
        healthCurrent = json.getInt("health");
        healthMax = json.getInt("max_health");
        coherence = json.getInt("coherence");
        coherenceReal = json.getInt("real_coherence");
        experienceCurrent = json.getInt("experience");
        experienceForNextLevel = json.getInt("experience_to_level");
    }

}
