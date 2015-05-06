package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Gender;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class PlaceAccountInfo {

    public final int id;
    public final String name;
    public final Integer clanId;
    public final int heroId;
    public final String heroName;
    public final Race heroRace;
    public final Gender heroGender;
    public final int heroLevel;

    public PlaceAccountInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        clanId = ObjectUtils.getOptionalInteger(json, "clan");

        final JSONObject heroJson = json.getJSONObject("hero");
        heroId = heroJson.getInt("id");
        heroName = heroJson.getString("name");
        heroRace = ObjectUtils.getEnumForCode(Race.class, heroJson.getInt("race"));
        heroGender = ObjectUtils.getEnumForCode(Gender.class, heroJson.getInt("gender"));
        heroLevel = heroJson.getInt("level");
    }

}
