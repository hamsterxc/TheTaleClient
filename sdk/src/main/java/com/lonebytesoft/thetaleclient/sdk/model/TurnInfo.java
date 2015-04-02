package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public class TurnInfo {

    public final int turnNumber;
    public final String verboseDate;
    public final String verboseTime;

    public TurnInfo(final JSONObject json) throws JSONException {
        this.turnNumber = json.getInt("number");
        this.verboseDate = json.getString("verbose_date");
        this.verboseTime = json.getString("verbose_time");
    }

}
