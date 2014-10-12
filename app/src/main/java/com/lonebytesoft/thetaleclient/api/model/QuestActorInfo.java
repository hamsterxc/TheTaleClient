package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class QuestActorInfo implements Parcelable {

    public final String name;
    public final QuestActorType type;

    public final QuestActorPersonInfo personInfo;
    public final QuestActorPlaceInfo placeInfo;
    public final QuestActorSpendingInfo spendingInfo;

    public QuestActorInfo(final JSONObject json) throws JSONException {
        name = json.getString("name");
        type = ObjectUtils.getEnumForCode(QuestActorType.class, json.getInt("type"));
        switch(type) {
            case PLACE:
                personInfo = null;
                placeInfo = ObjectUtils.getModelFromJson(QuestActorPlaceInfo.class, json.getJSONObject("info"));
                spendingInfo = null;
                break;

            case PERSON:
                personInfo = ObjectUtils.getModelFromJson(QuestActorPersonInfo.class, json.getJSONObject("info"));
                placeInfo = null;
                spendingInfo = null;
                break;

            case SPENDING:
                personInfo = null;
                placeInfo = null;
                spendingInfo = ObjectUtils.getModelFromJson(QuestActorSpendingInfo.class, json.getJSONObject("info"));
                break;

            default:
                personInfo = null;
                placeInfo = null;
                spendingInfo = null;
        }
    }

    // Parcelable stuff

    private QuestActorInfo(final Parcel in) {
        name = in.readString();
        type = QuestActorType.values()[in.readInt()];
        personInfo = in.readParcelable(QuestActorPersonInfo.class.getClassLoader());
        placeInfo = in.readParcelable(QuestActorPlaceInfo.class.getClassLoader());
        spendingInfo = in.readParcelable(QuestActorSpendingInfo.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(type.ordinal());
        out.writeParcelable(personInfo, 0);
        out.writeParcelable(placeInfo, 0);
        out.writeParcelable(spendingInfo, 0);
    }

    public static final Creator<QuestActorInfo> CREATOR = new Creator<QuestActorInfo>() {
        @Override
        public QuestActorInfo createFromParcel(Parcel source) {
            return new QuestActorInfo(source);
        }

        @Override
        public QuestActorInfo[] newArray(int size) {
            return new QuestActorInfo[size];
        }
    };

}
