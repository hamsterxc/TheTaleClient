package com.lonebytesoft.thetaleclient.desktop;

import com.lonebytesoft.thetaleclient.sdk.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.exception.HttpException;
import com.lonebytesoft.thetaleclient.sdk.exception.UpdateException;
import com.lonebytesoft.thetaleclient.sdk.helper.MapHelper;
import com.lonebytesoft.thetaleclient.sdk.model.HeroInfo;
import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.InfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.MapRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

public class Application {

    private static final String CLIENT_ID = "com.lonebytesoft.thetaleclient.sdk-26";

    /**
     * Draws map with all heroes that were active in last hour
     * Output: map.png in working directory
     */
    public static void main(String[] args) throws ApiException, JSONException {
        final Document document = Jsoup.parse(get("http://the-tale.org/accounts/"));
        final Elements elements = document.select("div.pagination");
        final Elements pageLinks = elements.get(0).children().get(0).children();
        final int pagesCount = Integer.parseInt(pageLinks.get(pageLinks.size() - 1).child(0).ownText());

        final int activePeriod = 60 * 60; // 1 hour
        final long currentTime = System.currentTimeMillis() / 1000;
        final List<HeroInfo> heroes = Collections.synchronizedList(new ArrayList<HeroInfo>());
        final ExecutorService executorService = Executors.newFixedThreadPool(20);
        final AtomicInteger countPages = new AtomicInteger(0);
        final AtomicInteger countHeroes = new AtomicInteger(0);

        for(int i = 1; i <= pagesCount; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    final Document document = Jsoup.parse(get(
                            String.format("http://the-tale.org/accounts/?prefix=&page=%d", index)));
                    final Elements elements = document.select("tr.pgf-account-record");
                    final int elementsCount = elements.size();
                    for(int elementIndex = 0; elementIndex < elementsCount; elementIndex++) {
                        final Element linkElement = elements.get(elementIndex).children().get(0).children().get(0);
                        final String url = linkElement.attr("href");
                        try {
                            final GameInfoResponse gameInfoResponse = new GameInfoRequest(CLIENT_ID,
                                    Integer.decode(url.substring(url.lastIndexOf('/') + 1))).execute();
                            if(gameInfoResponse.account.lastVisitTime + activePeriod > currentTime) {
                                heroes.add(gameInfoResponse.account.hero);
                                System.out.print(String.format("\r%d/%d pages, %d heroes",
                                        countPages.get(), pagesCount, countHeroes.incrementAndGet()));
                            }
                        } catch(UpdateException e) {
                            System.err.println("\rError: Updating");
                            System.exit(0);
                        } catch(ApiException e) {
                            if(e instanceof HttpException) {
                                System.err.println(String.format("\rError: HTTP %d", ((HttpException) e).getHttpStatusCode()));
                            } else {
                                System.err.println("\r" + e.getMessage());
                            }
                            System.out.print(String.format("\r%d/%d pages, %d heroes",
                                    countPages.get(), pagesCount, countHeroes.get()));
                            elementIndex--;
                        }
                    }
                    System.out.print(String.format("\r%d/%d pages, %d heroes",
                            countPages.incrementAndGet(), pagesCount, countHeroes.get()));
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            System.out.println();
        } catch (InterruptedException e) {
            System.err.println("\n" + e.getMessage());
        }

        final GameInfoResponse gameInfoResponse = new GameInfoRequest(CLIENT_ID).execute();
        final InfoResponse infoResponse = new InfoRequest(CLIENT_ID).execute();
        final MapResponse mapResponse = new MapRequest(infoResponse.dynamicContentUrl, gameInfoResponse.mapVersion).execute();

        try {
            final Point size = MapHelper.getMapSize(mapResponse);
            final BufferedImage mapImage = MapHelper.getMapImage(
                    size.x, size.y, MapStyle.STANDARD, true,
                    mapResponse, infoResponse.staticContentUrl, heroes, false);
            if(mapImage == null) {
                System.err.println("Could not get map image");
            } else {
                ImageIO.write(mapImage, "png", new File("map.png"));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(String.format("Done: %d s", System.currentTimeMillis() / 1000 - currentTime));
    }

    private static String get(final String url) {
        final HttpClient httpClient = HttpClients.createDefault();
        final HttpUriRequest httpUriRequest = new HttpGet(url);
        try {
            final HttpEntity httpEntity = httpClient.execute(httpUriRequest).getEntity();
            if(httpEntity == null) {
                return null;
            } else {
                return EntityUtils.toString(httpEntity);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}
