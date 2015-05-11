package com.lonebytesoft.thetaleclient.sdkandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.sdk.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.CardType;
import com.lonebytesoft.thetaleclient.sdk.model.CardInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 23.04.2015
 */
public class CardInfoParcelable extends CardInfo implements Parcelable {

    private static JSONObject getJson(final int id, final CardType type, final CardRarity rarity,
                                      final String name, final boolean isTradable) {
        try {
            final JSONObject json = new JSONObject();
            json.put("uid", id);
            json.put("type", type.code);
            json.put("rarity", rarity.code);
            json.put("name", name);
            json.put("auction", isTradable);
            return json;
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private CardInfoParcelable(final Parcel in) throws JSONException {
        super(getJson(
                in.readInt(),
                ObjectUtils.getEnumForCode(CardType.class, in.readInt()),
                ObjectUtils.getEnumForCode(CardRarity.class, in.readInt()),
                in.readString(), in.readInt() != 0));
    }

    public CardInfoParcelable(final CardInfo cardInfo) throws JSONException {
        super(getJson(cardInfo.id, cardInfo.type, cardInfo.rarity, cardInfo.name, cardInfo.isTradable));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(type.code);
        out.writeInt(rarity.code);
        out.writeString(name);
        out.writeInt(isTradable ? 1 : 0);
    }

    public static final Parcelable.Creator<CardInfoParcelable> CREATOR = new Parcelable.Creator<CardInfoParcelable>() {
        @Override
        public CardInfoParcelable createFromParcel(Parcel source) {
            try {
                return new CardInfoParcelable(source);
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        public CardInfoParcelable[] newArray(int size) {
            return new CardInfoParcelable[size];
        }
    };

}
