package com.lonebytesoft.thetaleclient.sdk.request;

import com.lonebytesoft.thetaleclient.sdk.AbstractApiGetRequest;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.response.MarketResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author Hamster
 * @since 27.04.2015
 */
public class MarketRequest extends AbstractApiGetRequest<MarketResponse> {

    private int page;

    @Override
    protected String getUrl() {
        return String.format("http://the-tale.org/market/?order_by=2&page=%d", page);
    }

    @Override
    public MarketResponse execute() throws ApiException {
        final MarketResponse response = new MarketResponse();
        page = 0;

        final Document marketDocument = Jsoup.parse(executeRequest());
        final Elements marketElements = marketDocument.select("div.pagination");
        if(marketElements.size() == 0) {
            throw new ApiException();
        }

        final Elements pageLinks = marketElements.get(0).children().get(0).children();
        final int pagesCount = Integer.parseInt(pageLinks.get(pageLinks.size() - 1).child(0).ownText());
        for(page = 1; page <= pagesCount; page++) {
            if(!response.addPageData(executeRequest())) {
                throw new ApiException();
            }
        }
        return response;
    }

}
