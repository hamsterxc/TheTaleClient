package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class QuestChoiceInfo {

    public final String id;
    public final String description;

    public QuestChoiceInfo(final JSONObject json) throws JSONException {
        id = json.getString("id");
        description = json.getString("description");
    }

}
