package com.lonebytesoft.thetaleclient.apisdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.sdk.dictionary.CompanionSpecies;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONException;
import com.lonebytesoft.thetaleclient.sdk.lib.org.json.JSONObject;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

/**
 * @author Hamster
 * @since 17.04.2015
 */
public class CompanionInfo extends com.lonebytesoft.thetaleclient.sdk.model.CompanionInfo implements Parcelable {

    private static JSONObject getJson(
            final CompanionSpecies species, final String name, final int healthCurrent, final int healthMax,
            final int coherence, final int coherenceReal, final int experienceCurrent, final int experienceForNextLevel) {
        try {
            final JSONObject json = new JSONObject();
            json.put("type", species == null ? -1 : species.code);
            json.put("name", name);
            json.put("health", healthCurrent);
            json.put("max_health", healthMax);
            json.put("coherence", coherence);
            json.put("real_coherence", coherenceReal);
            json.put("experience", experienceCurrent);
            json.put("experience_to_level", experienceForNextLevel);
            return json;
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private CompanionInfo(final Parcel in) {
        super(getJson(
                ObjectUtils.getEnumForCode(CompanionSpecies.class, in.readInt()),
                in.readString(), in.readInt(), in.readInt(),
                in.readInt(), in.readInt(), in.readInt(), in.readInt()));
    }

    public CompanionInfo(final com.lonebytesoft.thetaleclient.sdk.model.CompanionInfo companionInfo) {
        super(getJson(
                companionInfo.species, companionInfo.name, companionInfo.healthCurrent, companionInfo.healthMax,
                companionInfo.coherence, companionInfo.coherenceReal,
                companionInfo.experienceCurrent, companionInfo.experienceForNextLevel));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(species == null ? -1 : species.code);
        out.writeString(name);
        out.writeInt(healthCurrent);
        out.writeInt(healthMax);
        out.writeInt(coherence);
        out.writeInt(experienceCurrent);
        out.writeInt(experienceForNextLevel);
    }

    public static final Creator<CompanionInfo> CREATOR = new Creator<CompanionInfo>() {
        @Override
        public CompanionInfo createFromParcel(Parcel source) {
            return new CompanionInfo(source);
        }

        @Override
        public CompanionInfo[] newArray(int size) {
            return new CompanionInfo[size];
        }
    };

}
