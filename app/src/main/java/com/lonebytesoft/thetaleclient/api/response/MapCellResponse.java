package com.lonebytesoft.thetaleclient.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.lonebytesoft.thetaleclient.api.dictionary.MapCellType;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public final List<String> terrain;

    public MapCellResponse(final String response) throws JSONException {
        final Document document = Jsoup.parse(response);

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

        if(type == MapCellType.PLACE) {
            description = document.select("div#pgf-cell-description").first().html();
        } else {
            description = null;
        }

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

        terrain = new ArrayList<>();
        in.readStringList(terrain);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(type.ordinal());
        out.writeString(title);
        out.writeString(description);
        out.writeStringList(new ArrayList<>(parameters.keySet()));
        out.writeStringList(new ArrayList<>(parameters.values()));
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
