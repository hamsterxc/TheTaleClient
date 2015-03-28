package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.CardRarity;
import com.lonebytesoft.thetaleclient.api.dictionary.CardType;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 16.02.2015
 */
public class CardInfo implements Comparable<CardInfo>, Parcelable {

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

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof CardInfo)) {
            return false;
        }

        final CardInfo another = (CardInfo) o;
        return (another.type == type)
                && another.name.equals(name)
                && (another.isTradable == isTradable);
    }

    @Override
    public int compareTo(CardInfo another) {
        if(type == another.type) {
            if(name.equals(another.name)) {
                if(isTradable) {
                    return another.isTradable ? 0 : 1;
                } else {
                    return another.isTradable ? -1 : 0;
                }
            } else {
                return name.compareToIgnoreCase(another.name);
            }
        } else {
            if((type == null) || (another.type == null)) {
                if(rarity == another.rarity) {
                    if(name.equals(another.name)) {
                        if(isTradable) {
                            return another.isTradable ? 0 : 1;
                        } else {
                            return another.isTradable ? -1 : 0;
                        }
                    } else {
                        return name.compareToIgnoreCase(another.name);
                    }
                } else {
                    return rarity.ordinal() - another.rarity.ordinal();
                }
            } else {
                return type.ordinal() - another.type.ordinal();
            }
        }
    }

    // parcelable stuff

    private CardInfo(final Parcel in) {
        id = in.readInt();

        final int typeIndex = in.readInt();
        type = typeIndex < 0 ? null : CardType.values()[typeIndex];

        rarity = CardRarity.values()[in.readInt()];
        name = in.readString();
        isTradable = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(type == null ? -1 : type.ordinal());
        out.writeInt(rarity.ordinal());
        out.writeString(name);
        out.writeInt(isTradable ? 1 : 0);
    }

    public static final Parcelable.Creator<CardInfo> CREATOR = new Parcelable.Creator<CardInfo>() {
        @Override
        public CardInfo createFromParcel(Parcel source) {
            return new CardInfo(source);
        }

        @Override
        public CardInfo[] newArray(int size) {
            return new CardInfo[size];
        }
    };

}
