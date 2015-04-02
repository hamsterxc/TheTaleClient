package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Gender;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Profession;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class QuestActorDetailsPerson implements QuestActorDetails {

    public final int id;
    public final String name;
    public final Race race;
    public final Gender gender;
    public final Profession profession;
    public final String mastery;
    public final int placeId;

    public QuestActorDetailsPerson(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        gender = ObjectUtils.getEnumForCode(Gender.class, json.getInt("gender"));
        profession = ObjectUtils.getEnumForCode(Profession.class, json.getInt("profession"));
        mastery = json.getString("mastery_verbose");
        placeId = json.getInt("place");
    }

}
