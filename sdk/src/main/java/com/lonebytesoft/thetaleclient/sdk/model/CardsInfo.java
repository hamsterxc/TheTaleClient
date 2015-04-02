package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class CardsInfo {

    public final Collection<CardInfo> cards;
    public final int cardHelpCurrent;
    public final int cardHelpBarrier;

    public CardsInfo(final JSONObject json) throws JSONException {
        final JSONArray cardsJson = json.getJSONArray("cards");
        final int size = cardsJson.length();
        cards = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            final CardInfo card = ObjectUtils.getModelFromJson(CardInfo.class, cardsJson.getJSONObject(i));
            if(card != null) {
                cards.add(card);
            }
        }

        cardHelpCurrent = json.getInt("help_count");
        cardHelpBarrier = json.getInt("help_barrier");
    }

}
