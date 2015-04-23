package com.lonebytesoft.thetaleclient.apisdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONArray;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONException;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONObject;
import com.lonebytesoft.thetaleclient.sdk.model.ArtifactInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

/**
 * @author Hamster
 * @since 23.04.2015
 */
public class ArtifactInfoParcelable extends ArtifactInfo implements Parcelable {

    private static JSONObject getJson(
            final int id, final String name, final int powerPhysical, final int powerMagical,
            final ArtifactType type, final int integrityCurrent, final int integrityTotal,
            final ArtifactRarity rarity, final ArtifactEffect effect, final ArtifactEffect effectSpecial,
            final double rating, final boolean isEquippable) {
        try {
            final JSONObject json = new JSONObject();
            json.put("id", id);
            json.put("name", name);
            json.put("type", type.code);
            json.put("equipped", isEquippable);
            json.put("effect", effect.code);
            json.put("special_effect", effectSpecial.code);
            if(type != ArtifactType.JUNK) {
                final JSONArray power = new JSONArray();
                power.put(powerPhysical);
                power.put(powerMagical);
                json.put("power", power);

                final JSONArray integrity = new JSONArray();
                integrity.put(integrityCurrent);
                integrity.put(integrityTotal);
                json.put("integrity", integrity);

                json.put("rarity", rarity.code);
                json.put("preference_rating", rating);
            }
            return json;
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private ArtifactInfoParcelable(final Parcel in) {
        super(getJson(
                in.readInt(), in.readString(), in.readInt(), in.readInt(),
                ObjectUtils.getEnumForCode(ArtifactType.class, in.readInt()),
                in.readInt(), in.readInt(),
                ObjectUtils.getEnumForCode(ArtifactRarity.class, in.readInt()),
                ObjectUtils.getEnumForCode(ArtifactEffect.class, in.readInt()),
                ObjectUtils.getEnumForCode(ArtifactEffect.class, in.readInt()),
                in.readDouble(), in.readInt() != 0));
    }

    public ArtifactInfoParcelable(final ArtifactInfo artifactInfo) {
        super(getJson(
                artifactInfo.id, artifactInfo.name, artifactInfo.powerPhysical, artifactInfo.powerMagical,
                artifactInfo.type, artifactInfo.integrityCurrent, artifactInfo.integrityTotal,
                artifactInfo.rarity, artifactInfo.effect, artifactInfo.effectSpecial,
                artifactInfo.rating, artifactInfo.isEquippable));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeInt(powerPhysical);
        out.writeInt(powerMagical);
        out.writeInt(type.code);
        out.writeInt(integrityCurrent);
        out.writeInt(integrityTotal);
        out.writeInt(rarity.code);
        out.writeInt(effect.code);
        out.writeInt(effectSpecial.code);
        out.writeDouble(rating);
        out.writeInt(isEquippable ? 1 : 0);
    }

    public static final Creator<ArtifactInfoParcelable> CREATOR = new Creator<ArtifactInfoParcelable>() {
        @Override
        public ArtifactInfoParcelable createFromParcel(Parcel source) {
            return new ArtifactInfoParcelable(source);
        }

        @Override
        public ArtifactInfoParcelable[] newArray(int size) {
            return new ArtifactInfoParcelable[size];
        }
    };

}
