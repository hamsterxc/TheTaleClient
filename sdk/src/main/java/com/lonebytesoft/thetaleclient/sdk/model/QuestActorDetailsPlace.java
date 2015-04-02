package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class QuestActorDetailsPlace implements QuestActorDetails {

    public final int id;
    public final String name;

    public QuestActorDetailsPlace(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
    }

}
