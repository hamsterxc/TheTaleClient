package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardType;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class CardInfo implements Comparable<CardInfo> {

    public final int id;
    public final CardType type;
    public final CardRarity rarity;
    public final String name;
    public final boolean isTradable;

    public CardInfo(final JSONObject json) throws JSONException {
        id = json.getInt("uid");
        type = ObjectUtils.getEnumForCode(CardType.class, json.getInt("type"));
        rarity = ObjectUtils.getEnumForCode(CardRarity.class, json.getInt("rarity"));
        name = json.getString("name");
        isTradable = json.getBoolean("auction");
    }

    /**
     * Sort by rarity asc, then by name asc, then not tradable first, tradable second
     */
    @Override
    public int compareTo(CardInfo another) {
        if(rarity == another.rarity) {
            if(name.equals(another.name)) {
                if(isTradable) {
                    return another.isTradable ? 0 : 1;
                } else {
                    return another.isTradable ? -1 : 0;
                }
            } else {
                return name.compareTo(another.name);
            }
        } else {
            return rarity.code - another.rarity.code;
        }
    }

}
