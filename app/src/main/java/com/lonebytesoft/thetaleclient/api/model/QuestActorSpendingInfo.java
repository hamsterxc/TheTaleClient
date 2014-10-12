package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class QuestActorSpendingInfo implements Parcelable {

    public final String goal;
    public final String description;

    public QuestActorSpendingInfo(final JSONObject json) throws JSONException {
        goal = json.getString("goal");
        description = json.optString("description"); // todo undocumented feature
    }

    // Parcelable stuff

    private QuestActorSpendingInfo(final Parcel in) {
        goal = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(goal);
        out.writeString(description);
    }

    public static final Parcelable.Creator<QuestActorSpendingInfo> CREATOR = new Parcelable.Creator<QuestActorSpendingInfo>() {
        @Override
        public QuestActorSpendingInfo createFromParcel(Parcel source) {
            return new QuestActorSpendingInfo(source);
        }

        @Override
        public QuestActorSpendingInfo[] newArray(int size) {
            return new QuestActorSpendingInfo[size];
        }
    };

}
