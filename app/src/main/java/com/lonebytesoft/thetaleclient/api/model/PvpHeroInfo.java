package com.lonebytesoft.thetaleclient.api.model;

import com.lonebytesoft.thetaleclient.api.dictionary.PvpAbility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class PvpHeroInfo {

    public final int advantage;
    public final int effectiveness;
    public final Map<PvpAbility, Double> probabilities;
    public final int energy;
    public final int energyGrowthSpeed;

    public PvpHeroInfo(final JSONObject json) throws JSONException {
        advantage = json.getInt("advantage");
        effectiveness = json.getInt("effectiveness");
        energy = json.getInt("energy");
        energyGrowthSpeed = json.getInt("energy_speed");

        probabilities = new HashMap<>(PvpAbility.values().length);
        for(final PvpAbility pvpAbility : PvpAbility.values()) {
            probabilities.put(pvpAbility, json.getDouble(pvpAbility.getCode()));
        }
    }

}
