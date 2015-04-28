package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdk.model.MarketLotInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Hamster
 * @since 27.04.2015
 */
public class MarketResponse extends AbstractResponse {

    public final Collection<MarketLotInfo> lots;

    public MarketResponse() {
        super(null);
        lots = new ArrayList<>();
    }

    public boolean addPageData(final String pageData) {
        if(pageData != null) {
            final Document document = Jsoup.parse(pageData);
            final Elements elements = document.select("table tr");
            final int elementsCount = elements.size();
            final Collection<MarketLotInfo> pageLots = new ArrayList<>(elementsCount - 1);
            for(int i = 1; i < elementsCount; i++) {
                final Elements lotElements = elements.get(i).children();
                try {
                    pageLots.add(new MarketLotInfo(lotElements.get(0).text(), lotElements.get(1).text(), lotElements.get(2).text()));
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }

            lots.addAll(pageLots);
            return true;
        } else {
            return false;
        }
    }

}
