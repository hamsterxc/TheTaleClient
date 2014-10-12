package com.lonebytesoft.thetaleclient.api.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class DiaryEntry {

    public final int timestamp;
    public final String time;
    public final String date;
    public final String text;

    public DiaryEntry(final JSONObject json) throws JSONException {
        timestamp = json.getInt("timestamp");
        time = json.getString("time");
        date = json.getString("date");
        text = json.getString("text");
    }

}
