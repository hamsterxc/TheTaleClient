package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class ArtifactInfo implements Parcelable {

    public final int id;
    public final String name;
    public final int powerPhysical;
    public final int powerMagical;
    public final ArtifactType type;
    public final int integrityCurrent;
    public final int integrityTotal;
    public final ArtifactRarity rarity;
    public final ArtifactEffect effect;
    public final ArtifactEffect effectSpecial;
    public final double rating;
    public final boolean isEquippable;

    public ArtifactInfo(final JSONObject json) throws JSONException {
        name = json.getString("name");
        type = ObjectUtils.getEnumForCode(ArtifactType.class, json.getInt("type"));
        if(type == ArtifactType.JUNK) {
            powerPhysical = 0;
            powerMagical = 0;
            integrityCurrent = 0;
            integrityTotal = 0;
            rarity = ArtifactRarity.COMMON;
            rating = 0;
        } else {
            powerPhysical = json.getJSONArray("power").getInt(0);
            powerMagical = json.getJSONArray("power").getInt(1);
            integrityCurrent = json.getJSONArray("integrity").getInt(0);
            integrityTotal = json.getJSONArray("integrity").getInt(1);
            rarity = ObjectUtils.getEnumForCode(ArtifactRarity.class, json.getInt("rarity"));
            rating = json.getDouble("preference_rating");
        }
        effect = ObjectUtils.getEnumForCode(ArtifactEffect.class, json.getInt("effect"));
        effectSpecial = ObjectUtils.getEnumForCode(ArtifactEffect.class, json.getInt("special_effect"));
        isEquippable = json.getBoolean("equipped");
        id = json.getInt("id");
    }

    // Parcelable stuff

    private ArtifactInfo(final Parcel in) {
        id = in.readInt();
        name = in.readString();
        powerPhysical = in.readInt();
        powerMagical = in.readInt();
        type = ArtifactType.values()[in.readInt()];
        integrityCurrent = in.readInt();
        integrityTotal = in.readInt();
        rarity = ArtifactRarity.values()[in.readInt()];
        effect = ArtifactEffect.values()[in.readInt()];
        effectSpecial = ArtifactEffect.values()[in.readInt()];
        rating = in.readDouble();
        isEquippable = in.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeInt(powerPhysical);
        out.writeInt(powerMagical);
        out.writeInt(type.ordinal());
        out.writeInt(integrityCurrent);
        out.writeInt(integrityTotal);
        out.writeInt(rarity.ordinal());
        out.writeInt(effect.ordinal());
        out.writeInt(effectSpecial.ordinal());
        out.writeDouble(rating);
        out.writeInt(isEquippable ? 1 : 0);
    }

    public static final Creator<ArtifactInfo> CREATOR = new Creator<ArtifactInfo>() {
        @Override
        public ArtifactInfo createFromParcel(Parcel source) {
            return new ArtifactInfo(source);
        }

        @Override
        public ArtifactInfo[] newArray(int size) {
            return new ArtifactInfo[size];
        }
    };

}
