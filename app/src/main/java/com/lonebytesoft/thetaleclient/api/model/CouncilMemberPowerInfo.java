package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 05.05.2015
 */
public class CouncilMemberPowerInfo {

    public final double power;
    public final double bonusPositive;
    public final double bonusNegative;

    public CouncilMemberPowerInfo(final JSONObject json) throws JSONException {
        power = json.getDouble("percents");
        bonusPositive = json.getDouble("positive_bonus");
        bonusNegative = json.getDouble("negative_bonus");
    }

}
