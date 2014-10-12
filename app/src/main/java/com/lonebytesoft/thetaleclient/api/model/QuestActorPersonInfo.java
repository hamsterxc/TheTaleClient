package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.Gender;
import com.lonebytesoft.thetaleclient.api.dictionary.Profession;
import com.lonebytesoft.thetaleclient.api.dictionary.Race;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class QuestActorPersonInfo implements Parcelable {

    public final int id;
    public final String name;
    public final Race race;
    public final Gender gender;
    public final Profession profession;
    public final String mastery;
    public final int placeId;

    public QuestActorPersonInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        gender = ObjectUtils.getEnumForCode(Gender.class, json.getInt("gender"));
        profession = ObjectUtils.getEnumForCode(Profession.class, json.getInt("profession"));
        mastery = json.getString("mastery_verbose");
        placeId = json.getInt("place");
    }

    // Parcelable stuff

    private QuestActorPersonInfo(final Parcel in) {
        id = in.readInt();
        name = in.readString();
        race = Race.values()[in.readInt()];
        gender = Gender.values()[in.readInt()];
        profession = Profession.values()[in.readInt()];
        mastery = in.readString();
        placeId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeInt(race.ordinal());
        out.writeInt(gender.ordinal());
        out.writeInt(profession.ordinal());
        out.writeString(mastery);
        out.writeInt(placeId);
    }

    public static final Creator<QuestActorPersonInfo> CREATOR = new Creator<QuestActorPersonInfo>() {
        @Override
        public QuestActorPersonInfo createFromParcel(Parcel source) {
            return new QuestActorPersonInfo(source);
        }

        @Override
        public QuestActorPersonInfo[] newArray(int size) {
            return new QuestActorPersonInfo[size];
        }
    };

}
