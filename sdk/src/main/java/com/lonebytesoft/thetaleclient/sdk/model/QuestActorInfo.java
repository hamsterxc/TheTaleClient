package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class QuestActorInfo {

    public final String name;
    public final QuestActorType type;

    public final QuestActorDetails details;

    private static final Map<QuestActorType, Class<? extends QuestActorDetails>> questActorDetailsClasses;

    public QuestActorInfo(final JSONObject json) throws JSONException {
        name = json.getString("name");
        type = ObjectUtils.getEnumForCode(QuestActorType.class, json.getInt("type"));
        details = ObjectUtils.getModelFromJson(questActorDetailsClasses.get(type), json.getJSONObject("info"));
    }

    static {
        questActorDetailsClasses = new HashMap<>(QuestActorType.values().length);
        questActorDetailsClasses.put(QuestActorType.PLACE, QuestActorDetailsPlace.class);
        questActorDetailsClasses.put(QuestActorType.PERSON, QuestActorDetailsPerson.class);
        questActorDetailsClasses.put(QuestActorType.SPENDING, QuestActorDetailsSpending.class);
    }

}
