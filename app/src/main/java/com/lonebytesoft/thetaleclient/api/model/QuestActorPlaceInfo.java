package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class QuestActorPlaceInfo implements Parcelable {

    public final int id;
    public final String name;

    public QuestActorPlaceInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        name = json.getString("name");
    }

    // Parcelable stuff

    private QuestActorPlaceInfo(final Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
    }

    public static final Parcelable.Creator<QuestActorPlaceInfo> CREATOR = new Parcelable.Creator<QuestActorPlaceInfo>() {
        @Override
        public QuestActorPlaceInfo createFromParcel(Parcel source) {
            return new QuestActorPlaceInfo(source);
        }

        @Override
        public QuestActorPlaceInfo[] newArray(int size) {
            return new QuestActorPlaceInfo[size];
        }
    };

}
