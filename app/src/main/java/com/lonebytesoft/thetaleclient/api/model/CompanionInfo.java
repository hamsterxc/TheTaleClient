package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.CompanionSpecies;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 17.02.2015
 */
public class CompanionInfo implements Parcelable {

    public final CompanionSpecies species;
    public final String name;

    public final int healthCurrent;
    public final int healthMax;

    public final int coherence;
    public final int experienceCurrent;
    public final int experienceForNextLevel;

    public CompanionInfo(final JSONObject json) throws JSONException {
        species = ObjectUtils.getEnumForCode(CompanionSpecies.class, json.getInt("type"));
        name = json.getString("name");
        healthCurrent = json.getInt("health");
        healthMax = json.getInt("max_health");
        coherence = json.getInt("coherence");
        experienceCurrent = json.getInt("experience");
        experienceForNextLevel = json.getInt("experience_to_level");
    }

    // parcelable stuff

    private CompanionInfo(final Parcel in) {
        final int index = in.readInt();
        species = index == -1 ? null : CompanionSpecies.values()[index];
        name = in.readString();
        healthCurrent = in.readInt();
        healthMax = in.readInt();
        coherence = in.readInt();
        experienceCurrent = in.readInt();
        experienceForNextLevel = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(species == null ? -1 : species.ordinal());
        out.writeString(name);
        out.writeInt(healthCurrent);
        out.writeInt(healthMax);
        out.writeInt(coherence);
        out.writeInt(experienceCurrent);
        out.writeInt(experienceForNextLevel);
    }

    public static final Parcelable.Creator<CompanionInfo> CREATOR = new Parcelable.Creator<CompanionInfo>() {
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
