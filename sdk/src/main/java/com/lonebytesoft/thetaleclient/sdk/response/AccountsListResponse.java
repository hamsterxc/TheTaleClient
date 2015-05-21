package com.lonebytesoft.thetaleclient.sdk.response;

import com.lonebytesoft.thetaleclient.sdk.AbstractResponse;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.model.AccountShortInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 13.05.2015
 */
public class AccountsListResponse extends AbstractResponse {

    public final int pagesCount;
    public final List<AccountShortInfo> accounts;

    public AccountsListResponse(String response) throws ApiException {
        super(response);
        final Document document = Jsoup.parse(response);

        final Elements elementsPagination = document.select("div.pagination");
        if(elementsPagination.size() == 0) {
            pagesCount = 0;
            accounts = new ArrayList<>();
        } else {
            final Elements elementsPages = elementsPagination.get(0).children().get(0).children();
            pagesCount = Integer.parseInt(elementsPages.get(elementsPages.size() - 1).child(0).ownText());

            final Elements elementsRecords = document.select("tr.pgf-account-record");
            accounts = new ArrayList<>(elementsRecords.size());
            for(final Element elementRecord : elementsRecords) {
                final Elements cells = elementRecord.children();
                final Element accountLink = cells.get(0).children().get(0);
                final String url = accountLink.attr("href");
                accounts.add(new AccountShortInfo(
                        url.substring(url.lastIndexOf('/') + 1),
                        accountLink.ownText(),
                        cells.get(1).ownText(),
                        cells.get(2).attr("data-timestamp")));
            }
        }
    }

}
