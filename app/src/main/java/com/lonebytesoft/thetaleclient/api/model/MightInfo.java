package com.lonebytesoft.thetaleclient.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class MightInfo implements Parcelable {

    public final double value;
    public final double helpCriticalChance;
    public final double pvpEffectivenessBonus;
    public final double politicsPower;

    public MightInfo(final JSONObject json) throws JSONException {
        value = json.getDouble("value");
        helpCriticalChance = json.getDouble("crit_chance");
        pvpEffectivenessBonus = json.getDouble("pvp_effectiveness_bonus");
        politicsPower = json.getDouble("politics_power");
    }

    // Parcelable stuff

    private MightInfo(final Parcel in) {
        value = in.readDouble();
        helpCriticalChance = in.readDouble();
        pvpEffectivenessBonus = in.readDouble();
        politicsPower = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(value);
        out.writeDouble(helpCriticalChance);
        out.writeDouble(pvpEffectivenessBonus);
        out.writeDouble(politicsPower);
    }

    public static final Creator<MightInfo> CREATOR = new Creator<MightInfo>() {
        @Override
        public MightInfo createFromParcel(Parcel source) {
            return new MightInfo(source);
        }

        @Override
        public MightInfo[] newArray(int size) {
            return new MightInfo[size];
        }
    };

}
