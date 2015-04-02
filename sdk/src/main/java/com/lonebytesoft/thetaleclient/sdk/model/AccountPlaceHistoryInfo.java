package com.lonebytesoft.thetaleclient.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 23.03.2015
 */
public class AccountPlaceHistoryInfo implements Comparable<AccountPlaceHistoryInfo> {

    public final int placeId;
    public final String name;
    public final int helpCount;

    public AccountPlaceHistoryInfo(final JSONObject json) throws JSONException {
        placeId = json.getJSONObject("place").getInt("id");
        name = json.getJSONObject("place").getString("name");
        helpCount = json.getInt("count");
    }

    /**
     * Sort by help count desc, then by place name asc
     */
    @Override
    public int compareTo(AccountPlaceHistoryInfo another) {
        if (helpCount == another.helpCount) {
            return name.compareToIgnoreCase(another.name);
        } else {
            return another.helpCount - helpCount;
        }
    }

}
