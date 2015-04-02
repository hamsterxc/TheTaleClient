package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class QuestActorDetailsSpending implements QuestActorDetails {

    public final String goal;
    public final String description;

    public QuestActorDetailsSpending(final JSONObject json) throws JSONException {
        goal = json.getString("goal");
        description = json.getString("description"); // TODO undocumented feature
    }

}
