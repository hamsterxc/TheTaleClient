package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceChronicleEntry {

    public final String dateShort;
    public final String date;
    public final String text;

    public PlaceChronicleEntry(final JSONObject json) throws JSONException {
        dateShort = json.getString("date_short");
        date = json.getString("date");
        text = json.getString("text");
    }

}
