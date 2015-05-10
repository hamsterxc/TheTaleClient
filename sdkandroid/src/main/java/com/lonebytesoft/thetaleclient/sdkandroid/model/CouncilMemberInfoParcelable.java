package com.lonebytesoft.thetaleclient.sdkandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Gender;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Profession;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.model.CouncilMemberConnectionInfo;
import com.lonebytesoft.thetaleclient.sdk.model.CouncilMemberInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 07.05.2015
 */
public class CouncilMemberInfoParcelable extends CouncilMemberInfo implements Parcelable {

    public static JSONObject getJson(
            final int id, final String name, final Gender gender, final Race race,
            final Profession profession, final int newThreshold, final Integer buildingId,
            final double mastery, final String masteryVerbose,
            final double power, final double powerBonusPositive, final double powerBonusNegative,
            final List<Integer> friends, final List<Integer> enemies,
            final List<CouncilMemberConnectionInfo> connections) {
        try {
            final JSONObject json = new JSONObject();

            json.put("id", id);
            json.put("name", name);
            json.put("gender", gender.code);
            json.put("race", race.code);
            json.put("type", profession.code);
            json.put("unfreeze_in", newThreshold);

            if(buildingId == null) {
                json.put("building", JSONObject.NULL);
            } else {
                json.put("building", buildingId.intValue());
            }

            final JSONObject jsonMastery = new JSONObject();
            jsonMastery.put("value", mastery);
            jsonMastery.put("name", masteryVerbose);
            json.put("mastery", jsonMastery);

            final JSONObject jsonPower = new JSONObject();
            jsonPower.put("percents", power);
            jsonPower.put("positive_bonus", powerBonusPositive);
            jsonPower.put("negative_bonus", powerBonusNegative);
            json.put("power", jsonPower);

            final JSONArray friendsJson = new JSONArray();
            for(final int friendId : friends) {
                friendsJson.put(friendId);
            }
            final JSONArray enemiesJson = new JSONArray();
            for(final int enemyId : enemies) {
                enemiesJson.put(enemyId);
            }
            final JSONObject keepersJson = new JSONObject();
            keepersJson.put("friends", friendsJson);
            keepersJson.put("enemies", enemiesJson);
            json.put("keepers", keepersJson);

            final JSONArray connectionsJson = new JSONArray();
            for(final CouncilMemberConnectionInfo connectionInfo : connections) {
                final JSONArray connectionJson = new JSONArray();
                connectionJson.put(connectionInfo.type.code);
                connectionJson.put(connectionInfo.councilMemberId);
                connectionsJson.put(connectionJson);
            }
            json.put("connections", connectionsJson);

            return json;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public CouncilMemberInfoParcelable(final CouncilMemberInfo councilMemberInfo) throws JSONException {
        super(getJson(
                councilMemberInfo.id, councilMemberInfo.name, councilMemberInfo.gender, councilMemberInfo.race,
                councilMemberInfo.profession, councilMemberInfo.newThreshold, councilMemberInfo.buildingId,
                councilMemberInfo.mastery, councilMemberInfo.masteryVerbose,
                councilMemberInfo.power, councilMemberInfo.powerBonusPositive, councilMemberInfo.powerBonusNegative,
                councilMemberInfo.friends, councilMemberInfo.enemies, councilMemberInfo.connections));
    }

    public CouncilMemberInfoParcelable(final Parcel in) throws JSONException {
        super(getJson(
                in.readInt(), in.readString(), Gender.values()[in.readInt()], Race.values()[in.readInt()],
                Profession.values()[in.readInt()], in.readInt(), readBuildingIdFromParcel(in),
                in.readDouble(), in.readString(), in.readDouble(), in.readDouble(), in.readDouble(),
                readIntegerListFromParcel(in), readIntegerListFromParcel(in), readConnectionsFromParcel(in)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(name);
        out.writeInt(gender.ordinal());
        out.writeInt(race.ordinal());
        out.writeInt(profession.ordinal());
        out.writeInt(newThreshold);
        writeBuildingIdToParcel(out, buildingId);
        out.writeDouble(mastery);
        out.writeString(masteryVerbose);
        out.writeDouble(power);
        out.writeDouble(powerBonusPositive);
        out.writeDouble(powerBonusNegative);
        writeIntegerListToParcel(out, friends);
        writeIntegerListToParcel(out, enemies);
        writeConnectionsToParcel(out, connections);
    }

    private static void writeBuildingIdToParcel(final Parcel out, final Integer buildingId) {
        out.writeInt(buildingId == null ? Integer.MIN_VALUE : buildingId);
    }

    private static Integer readBuildingIdFromParcel(final Parcel in) {
        final int buildingId = in.readInt();
        return buildingId == Integer.MIN_VALUE ? null : buildingId;
    }

    private static void writeIntegerListToParcel(final Parcel out, final List<Integer> list) {
        out.writeInt(list.size());
        for(final int value : list) {
            out.writeInt(value);
        }
    }

    private static List<Integer> readIntegerListFromParcel(final Parcel in) {
        final int count = in.readInt();
        final int[] values = new int[count];
        in.readIntArray(values);
        final List<Integer> result = new ArrayList<>();
        for(final int value : values) {
            result.add(value);
        }
        return result;
    }

    private static void writeConnectionsToParcel(final Parcel out, final List<CouncilMemberConnectionInfo> connections) {
        out.writeInt(connections.size());
        for(final CouncilMemberConnectionInfo connection : connections) {
            out.writeInt(connection.type.code);
            out.writeInt(connection.councilMemberId);
        }
    }

    private static List<CouncilMemberConnectionInfo> readConnectionsFromParcel(final Parcel in) {
        final int count = in.readInt();
        final List<CouncilMemberConnectionInfo> result = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            final JSONObject connectionJson = new JSONObject();
            try {
                connectionJson.put("social_link", in.readInt());
                connectionJson.put("council_member", in.readInt());
                result.add(ObjectUtils.getModelFromJson(CouncilMemberConnectionInfo.class, connectionJson));
            } catch (JSONException ignored) {
            }
        }
        return result;
    }

    public static final Creator<CouncilMemberInfoParcelable> CREATOR = new Creator<CouncilMemberInfoParcelable>() {
        @Override
        public CouncilMemberInfoParcelable createFromParcel(Parcel source) {
            try {
                return new CouncilMemberInfoParcelable(source);
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        public CouncilMemberInfoParcelable[] newArray(int size) {
            return new CouncilMemberInfoParcelable[size];
        }
    };

}
