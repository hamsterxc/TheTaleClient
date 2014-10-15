package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.Race;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public class PlaceInfo implements Parcelable {

    public final int id;
    public final int size;
    public final Race race;
    public final String name;
    public final int x;
    public final int y;

    public PlaceInfo(final JSONObject json) throws JSONException {
        id = json.getInt("id");
        size = json.getInt("size");
        race = ObjectUtils.getEnumForCode(Race.class, json.getInt("race"));
        name = json.getString("name");
        x = json.getJSONObject("pos").getInt("x");
        y = json.getJSONObject("pos").getInt("y");
    }

    // Parcelable stuff

    private PlaceInfo(final Parcel in) {
        id = in.readInt();
        size = in.readInt();
        race = Race.values()[in.readInt()];
        name = in.readString();
        x = in.readInt();
        y = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(size);
        out.writeInt(race.ordinal());
        out.writeString(name);
        out.writeInt(x);
        out.writeInt(y);
    }

    public static final Parcelable.Creator<PlaceInfo> CREATOR = new Parcelable.Creator<PlaceInfo>() {
        @Override
        public PlaceInfo createFromParcel(Parcel source) {
            return new PlaceInfo(source);
        }

        @Override
        public PlaceInfo[] newArray(int size) {
            return new PlaceInfo[size];
        }
    };

}
