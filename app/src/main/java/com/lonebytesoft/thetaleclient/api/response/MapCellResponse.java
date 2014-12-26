package com.lonebytesoft.thetaleclient.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.Gender;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellType;
import com.lonebytesoft.thetaleclient.api.dictionary.Profession;
import com.lonebytesoft.thetaleclient.api.dictionary.ProficiencyLevel;
import com.lonebytesoft.thetaleclient.api.dictionary.Race;
import com.lonebytesoft.thetaleclient.api.model.PlaceCouncilMember;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsing html code to get cell info
 * Not quite reliable, really, but we don't have API for that
 * @author Hamster
 * @since 15.10.2014
 */
public class MapCellResponse implements Parcelable {

    public final MapCellType type;
    public final String title;
    public final String description;
    public final Map<String, String> parameters;
    public final List<PlaceCouncilMember> council;
    public final List<String> terrain;

    public MapCellResponse(final String response) throws JSONException {
        final Document document = Jsoup.parse(response);

        // cell type & title
        final Element titleElement = document.select("div.modal-header h3").first();
        switch(titleElement.children().size()) {
            case 0:
                type = MapCellType.TERRAIN;
                title = titleElement.ownText();
                break;

            case 1:
                type = MapCellType.BUILDING;
                title = titleElement.child(0).ownText();
                break;

            case 2:
                type = MapCellType.PLACE;
                title = titleElement.child(1).ownText();
                break;

            default:
                type = MapCellType.UNKNOWN;
                title = null;
                break;
        }

        // place: description & council
        if(type == MapCellType.PLACE) {
            description = document.select("div#pgf-cell-description").first().html();

            final Elements councilElements = document.select("div#pgf-cell-persons li.person-record");
            council = new ArrayList<>(councilElements.size());
            for(final Element councilMemberElement : councilElements) {
                final Elements rows = councilMemberElement.select("tr");

                final String description = rows.get(2).children().get(0).children().get(0).ownText();
                Gender gender = null;
                Race race = null;
                ProficiencyLevel proficiencyLevel = null;
                for(final Gender genderItem : Gender.values()) {
                    if(description.contains(genderItem.getName())) {
                        gender = genderItem;
                        break;
                    }
                }
                for(final Race raceItem : Race.values()) {
                    if(description.contains(raceItem.getName())) {
                        race = raceItem;
                        break;
                    }
                }
                for(final ProficiencyLevel proficiencyLevelItem : ProficiencyLevel.values()) {
                    if(description.contains(proficiencyLevelItem.getCode())) {
                        proficiencyLevel = proficiencyLevelItem;
                        break;
                    }
                }

                final String professionString = rows.get(0).children().get(0).attr("title");
                Profession profession = null;
                for(final Profession professionItem : Profession.values()) {
                    if(professionString.equals(professionItem.getName())) {
                        profession = professionItem;
                        break;
                    }
                }

                final Pattern powerPattern = Pattern.compile("(\\d+)%");
                final Matcher powerMatcher = powerPattern.matcher(rows.get(0).children().get(3).ownText());
                powerMatcher.find();

                final Pattern powerBonusPattern = Pattern.compile("([\\d\\.]+)%\\s+([\\d\\.]+)%");
                final Matcher powerBonusMatcher = powerBonusPattern.matcher(
                        rows.get(1).children().get(1).children().get(0).ownText());
                powerBonusMatcher.find();

                final Pattern friendsEnemiesPattern = Pattern.compile("(\\d+)/(\\d+)");
                final Elements additionalInfoElements = rows.get(2).children().get(0).children().get(1).children();
                final Matcher friendsEnemiesMatcher = friendsEnemiesPattern.matcher(
                        additionalInfoElements.get(additionalInfoElements.size() - 1).ownText());
                friendsEnemiesMatcher.find();

                council.add(new PlaceCouncilMember(
                        rows.get(0).children().get(1).ownText(),
                        gender, race, profession, proficiencyLevel,
                        Integer.parseInt(powerMatcher.group(1)),
                        Float.parseFloat(powerBonusMatcher.group(1)),
                        Float.parseFloat(powerBonusMatcher.group(2)),
                        Integer.parseInt(friendsEnemiesMatcher.group(1)),
                        Integer.parseInt(friendsEnemiesMatcher.group(2))
                ));
            }
        } else {
            description = null;
            council = null;
        }

        // place & building: parameters
        if((type == MapCellType.PLACE) || (type == MapCellType.BUILDING)) {
            parameters = new LinkedHashMap<>();
            final int indexStart;
            final int indexEnd;
            final Elements elements;

            if(type == MapCellType.PLACE) {
                elements = document.select("div#pgf-cell-place-parameters table tr");
                indexStart = 1;
                indexEnd = elements.size();
            } else {
                elements = document.select("div#pgf-cell-building table tr");
                indexStart = 0;
                indexEnd = elements.size() - 1;
            }

            for(int i = indexStart; i < indexEnd; i++) {
                final Elements parameterElements = elements.get(i).children();
                parameters.put(parameterElements.get(0).text(), parameterElements.get(1).text());
            }
        } else {
            parameters = null;
        }

        // terrain
        terrain = new ArrayList<>();
        for(final Element element : document.select("div#pgf-cell-map ul li")) {
            terrain.add(element.text());
        }
    }

    // Parcelable stuff

    private MapCellResponse(final Parcel in) {
        type = MapCellType.values()[in.readInt()];
        title = in.readString();
        description = in.readString();

        final List<String> parameterKeys = new ArrayList<>();
        in.readStringList(parameterKeys);
        final List<String> parameterValues = new ArrayList<>();
        in.readStringList(parameterValues);
        if(parameterKeys.size() == parameterValues.size()) {
            final int size = parameterKeys.size();
            parameters = new LinkedHashMap<>(size);
            for(int i = 0; i < size; i++) {
                parameters.put(parameterKeys.get(i), parameterValues.get(i));
            }
        } else {
            throw new IllegalStateException("Error reading MapCellResponse from parcel: could not read parameters");
        }

        council = Arrays.asList((PlaceCouncilMember[]) in.readParcelableArray(PlaceCouncilMember.class.getClassLoader()));

        terrain = new ArrayList<>();
        in.readStringList(terrain);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(type.ordinal());
        out.writeString(title);
        out.writeString(description);
        out.writeStringList(new ArrayList<>(parameters.keySet()));
        out.writeStringList(new ArrayList<>(parameters.values()));
        out.writeParcelableArray((PlaceCouncilMember[]) council.toArray(), flags);
        out.writeStringList(terrain);
    }

    public static final Parcelable.Creator<MapCellResponse> CREATOR = new Parcelable.Creator<MapCellResponse>() {
        @Override
        public MapCellResponse createFromParcel(Parcel source) {
            return new MapCellResponse(source);
        }

        @Override
        public MapCellResponse[] newArray(int size) {
            return new MapCellResponse[size];
        }
    };

}
