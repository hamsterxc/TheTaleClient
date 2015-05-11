package com.lonebytesoft.thetaleclient.sdkandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.sdk.dictionary.Gender;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Profession;
import com.lonebytesoft.thetaleclient.sdk.dictionary.QuestActorType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Race;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorDetailsPerson;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorDetailsPlace;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorDetailsSpending;
import com.lonebytesoft.thetaleclient.sdk.model.QuestActorInfo;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Hamster
 * @since 23.04.2015
 */
public class QuestActorInfoParcelable extends QuestActorInfo implements Parcelable {

    private static JSONObject getJsonPlace(final String name, final int placeId, final String placeName) {
        try {
            final JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("type", QuestActorType.PLACE.code);

            final JSONObject info = new JSONObject();
            info.put("id", placeId);
            info.put("name", placeName);
            json.put("info", info);

            return json;
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private static JSONObject getJsonPerson(final String name,
                                            final int personId, final String personName, final Race personRace, final Gender personGender,
                                            final Profession personProfession, final String personMastery, final int personPlaceId) {
        try {
            final JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("type", QuestActorType.PERSON.code);

            final JSONObject info = new JSONObject();
            info.put("id", personId);
            info.put("name", personName);
            info.put("race", personRace.code);
            info.put("gender", personGender.code);
            info.put("profession", personProfession.code);
            info.put("mastery_verbose", personMastery);
            info.put("place", personPlaceId);
            json.put("info", info);

            return json;
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private static JSONObject getJsonSpending(final String name, final String spendingGoal, final String spendingDescrption) {
        try {
            final JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("type", QuestActorType.SPENDING.code);

            final JSONObject info = new JSONObject();
            info.put("goal", spendingGoal);
            info.put("description", spendingDescrption);
            json.put("info", info);

            return json;
        } catch (JSONException ignored) {
            return new JSONObject();
        }
    }

    private static JSONObject getJson(final Parcel in) {
        final String name = in.readString();
        final QuestActorType type = ObjectUtils.getEnumForCode(QuestActorType.class, in.readInt());
        if(type == null) {
            return new JSONObject();
        } else {
            switch(type) {
                case PLACE:
                    return getJsonPlace(name, in.readInt(), in.readString());

                case PERSON:
                    return getJsonPerson(
                            name, in.readInt(), in.readString(),
                            ObjectUtils.getEnumForCode(Race.class, in.readInt()),
                            ObjectUtils.getEnumForCode(Gender.class, in.readInt()),
                            ObjectUtils.getEnumForCode(Profession.class, in.readInt()),
                            in.readString(), in.readInt());

                case SPENDING:
                    return getJsonSpending(name, in.readString(), in.readString());

                default:
                    return new JSONObject();
            }
        }
    }

    private static JSONObject getJson(final QuestActorInfo questActorInfo) {
        switch(questActorInfo.type) {
            case PLACE:
                final QuestActorDetailsPlace detailsPlace = (QuestActorDetailsPlace) questActorInfo.details;
                return getJsonPlace(questActorInfo.name, detailsPlace.id, detailsPlace.name);

            case PERSON:
                final QuestActorDetailsPerson detailsPerson = (QuestActorDetailsPerson) questActorInfo.details;
                return getJsonPerson(questActorInfo.name, detailsPerson.id, detailsPerson.name, detailsPerson.race,
                        detailsPerson.gender, detailsPerson.profession, detailsPerson.mastery, detailsPerson.placeId);

            case SPENDING:
                final QuestActorDetailsSpending detailsSpending = (QuestActorDetailsSpending) questActorInfo.details;
                return getJsonSpending(questActorInfo.name, detailsSpending.goal, detailsSpending.description);

            default:
                return new JSONObject();
        }
    }

    private QuestActorInfoParcelable(final Parcel in) throws JSONException {
        super(getJson(in));
    }

    public QuestActorInfoParcelable(final com.lonebytesoft.thetaleclient.sdk.model.QuestActorInfo questActorInfo)
            throws JSONException {
        super(getJson(questActorInfo));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(type.code);
        switch(type) {
            case PLACE:
                final QuestActorDetailsPlace detailsPlace = (QuestActorDetailsPlace) details;
                out.writeInt(detailsPlace.id);
                out.writeString(detailsPlace.name);
                break;

            case PERSON:
                final QuestActorDetailsPerson detailsPerson = (QuestActorDetailsPerson) details;
                out.writeInt(detailsPerson.id);
                out.writeString(detailsPerson.name);
                out.writeInt(detailsPerson.race.code);
                out.writeInt(detailsPerson.gender.code);
                out.writeInt(detailsPerson.profession.code);
                out.writeString(detailsPerson.mastery);
                out.writeInt(detailsPerson.placeId);
                break;

            case SPENDING:
                final QuestActorDetailsSpending detailsSpending = (QuestActorDetailsSpending) details;
                out.writeString(detailsSpending.goal);
                out.writeString(detailsSpending.description);
                break;
        }
    }

    public static final Creator<QuestActorInfoParcelable> CREATOR = new Creator<QuestActorInfoParcelable>() {
        @Override
        public QuestActorInfoParcelable createFromParcel(Parcel source) {
            try {
                return new QuestActorInfoParcelable(source);
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        public QuestActorInfoParcelable[] newArray(int size) {
            return new QuestActorInfoParcelable[size];
        }
    };

}
