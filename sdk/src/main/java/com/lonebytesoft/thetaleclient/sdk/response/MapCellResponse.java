package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.MapCellType;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public class MapCellResponse extends AbstractResponse {

    public final MapCellType type;
    public final String title;
    public final Map<String, String> parameters;
    public final List<String> terrain;

    public MapCellResponse(final String response) throws ApiException {
        super(response);
        final Document document = Jsoup.parse(response);

        // cell type & title
        final Elements titleElements = document.select("div.modal-header h3");
        if(titleElements.isEmpty()) {
            throw new ApiException();
        }
        final Element titleElement = titleElements.first();
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
                throw new ApiException();
        }

        // building: parameters
        if(type == MapCellType.BUILDING) {
            parameters = new LinkedHashMap<>();
            final Elements elements = document.select("div#pgf-cell-building table tr");
            final int count = elements.size() - 1;
            for(int i = 0; i < count; i++) {
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

}
