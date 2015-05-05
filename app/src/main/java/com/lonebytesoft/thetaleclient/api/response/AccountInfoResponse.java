package com.lonebytesoft.thetaleclient.api.response;

import com.lonebytesoft.thetaleclient.api.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.api.dictionary.RatingItem;
import com.lonebytesoft.thetaleclient.api.model.AccountPlaceHistoryInfo;
import com.lonebytesoft.thetaleclient.api.model.RatingItemInfo;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 20.02.2015
 */
public class AccountInfoResponse extends AbstractApiResponse {

    public int accountId;
    public boolean isRegistered;
    public String name;
    public int heroId;
    public List<AccountPlaceHistoryInfo> placesHistory;
    public double might;
    public int achievementPoints;
    public int collectionItemsCount;
    public int referralsCount;
    public Map<RatingItem, RatingItemInfo> ratings;
    public boolean canAffectGame;
    public String description;

    public AccountInfoResponse(final String response) throws JSONException {
        super(response);
    }

    @Override
    protected void parseData(JSONObject data) throws JSONException {
        accountId = data.getInt("id");
        isRegistered = data.getBoolean("registered");
        name = data.getString("name");
        heroId = data.getInt("hero_id");
        might = data.getDouble("might");
        achievementPoints = data.getInt("achievements");
        collectionItemsCount = data.getInt("collections");
        referralsCount = data.getInt("referrals");
        canAffectGame = data.getJSONObject("permissions").getBoolean("can_affect_game");
        description = data.getString("description");

        final JSONArray placesHistory = data.getJSONArray("places_history");
        final int placesSize = placesHistory.length();
        this.placesHistory = new ArrayList<>(placesSize);
        for(int i = 0; i < placesSize; i++) {
            this.placesHistory.add(ObjectUtils.getModelFromJson(AccountPlaceHistoryInfo.class, placesHistory.getJSONObject(i)));
        }

        final JSONObject ratings = data.getJSONObject("ratings");
        this.ratings = new LinkedHashMap<>();
        for(final RatingItem ratingItem : RatingItem.values()) {
            final JSONObject rating = ratings.optJSONObject(ratingItem.getCode());
            if(rating != null) {
                this.ratings.put(ratingItem, ObjectUtils.getModelFromJson(RatingItemInfo.class, rating));
            }
        }
    }

}
