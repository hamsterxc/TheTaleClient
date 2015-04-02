package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestType;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class QuestStepInfo {

    public final String id;
    public final QuestType type;
    public final String name;
    public final String heroAction;
    public final String currentChoice;
    public final List<QuestChoiceInfo> choices;
    public final int experience;
    public final int power;
    public final List<QuestActorInfo> actors;

    public QuestStepInfo(final JSONObject json) throws JSONException {
        type = ObjectUtils.getEnumForCode(QuestType.class, json.getString("type"));
        id = json.getString("uid");
        name = json.getString("name");
        heroAction = json.getString("action");
        currentChoice = ObjectUtils.getOptionalString(json, "choice");
        experience = json.getInt("experience");
        power = json.getInt("power");

        final JSONArray choicesJson = json.getJSONArray("choice_alternatives");
        final int choicesCount = choicesJson.length();
        choices = new ArrayList<>(choicesCount);
        for(int i = 0; i < choicesCount; i++) {
            choices.add(ObjectUtils.getModelFromJson(QuestChoiceInfo.class,
                    ObjectUtils.getObjectFromArray(choicesJson.getJSONArray(i), new String[]{"id", "description"})));
        }

        final JSONArray actorsJson = json.getJSONArray("actors");
        final int actorsCount = actorsJson.length();
        actors = new ArrayList<>(actorsCount);
        for(int i = 0; i < actorsCount; i++) {
            actors.add(ObjectUtils.getModelFromJson(QuestActorInfo.class,
                    ObjectUtils.getObjectFromArray(actorsJson.getJSONArray(i), new String[]{"name", "type", "info"})));
        }
    }

}
