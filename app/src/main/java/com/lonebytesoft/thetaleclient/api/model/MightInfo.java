package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class MightInfo {

    public final double value;
    public final double helpCriticalChance;
    public final double pvpEffectivenessBonus;

    public MightInfo(final JSONObject json) throws JSONException {
        value = json.getDouble("value");
        helpCriticalChance = json.getDouble("crit_chance");
        pvpEffectivenessBonus = json.getDouble("pvp_effectiveness_bonus");
    }

}
