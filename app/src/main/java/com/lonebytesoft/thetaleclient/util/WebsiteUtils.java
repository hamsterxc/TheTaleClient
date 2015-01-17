package com.lonebytesoft.thetaleclient.util;

import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

/**
 * @author Hamster
 * @since 17.01.2015
 */
public class WebsiteUtils {

    private static final int THREADS_COUNT = 10;
    private static final String PAGE_ACCOUNTS = "http://the-tale.org/accounts/";

    private static final Object lock = new Object();

    /**
     * Enums all game accounts using provided callback
     * Uses HTML parsing from http://the-tale.org/accounts/
     */
    public static void enumAccounts(final AccountCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int pagesCount;
                final HttpMethod httpMethod = HttpMethod.GET;
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpRequest httpRequest = httpMethod.getHttpRequest(PAGE_ACCOUNTS, null, null);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();

                    final Document document = Jsoup.parse(outputStream.toString());
                    final Elements elements = document.select("div.pagination");
                    final Elements pageLinks = elements.get(0).children().get(0).children();
                    pagesCount = Integer.parseInt(pageLinks.get(pageLinks.size() - 1).child(0).ownText());
                } catch(IOException e) {
                    callback.onError(ErrorType.GLOBAL, 0, e.getMessage());
                    return;
                }

                final Queue<Integer> profiles = new LinkedList<>();
                final Queue<Integer> pages = new LinkedList<>();
                for(int i = 1; i <= pagesCount; i++) {
                    pages.add(i);
                }

                final CountDownLatch latch = new CountDownLatch(1);
                getPageProfiles(pages, profiles, latch, callback);
                try {
                    latch.await();
                } catch(InterruptedException e) {
                    callback.onError(ErrorType.GLOBAL, 0, e.getMessage());
                    return;
                }

                for(int i = 0; i < THREADS_COUNT; i++) {
                    getProfiles(profiles, callback);
                }
            }
        }).start();
    }

    private static void getPageProfiles(final Queue<Integer> pages, final Queue<Integer> profiles,
                                        final CountDownLatch latch, final AccountCallback callback) {
        final Integer id;
        synchronized(lock) {
            id = pages.poll();
        }
        if(id == null) {
            latch.countDown();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpMethod httpMethod = HttpMethod.GET;
                final Map<String, String> getParams = new HashMap<>(2);
                getParams.put("prefix", "");
                getParams.put("page", String.valueOf(id));
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpRequest httpRequest = httpMethod.getHttpRequest(PAGE_ACCOUNTS, getParams, null);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();

                    final Document document = Jsoup.parse(outputStream.toString());
                    final Elements elements = document.select("tr.pgf-account-record");
                    for(final Element element : elements) {
                        final String link = element.children().get(0).children().get(0).attr("href");
                        profiles.add(Integer.parseInt(link.substring(link.lastIndexOf('/') + 1)));
                    }
                } catch(IOException e) {
                    callback.onError(ErrorType.PROFILES_PAGE, id, e.getMessage());
                }
                getPageProfiles(pages, profiles, latch, callback);
            }
        }).start();
    }

    private static void getProfiles(final Queue<Integer> profiles, final AccountCallback callback) {
        final Integer id;
        final Integer next;
        synchronized(lock) {
            id = profiles.poll();
            next = profiles.peek();
        }
        if(id == null) {
            return;
        }

        new GameInfoRequest(false).execute(id, new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                callback.processAccount(response);
                if(next == null) {
                    callback.onFinish();
                } else {
                    getProfiles(profiles, callback);
                }
            }

            @Override
            public void processError(GameInfoResponse response) {
                if(!"game.info.account.not_found".equals(response.errorCode)) {
                    callback.onError(ErrorType.PROFILE, id, response.errorMessage);
                }
                if(next == null) {
                    callback.onFinish();
                } else {
                    getProfiles(profiles, callback);
                }
            }
        }, false);
    }

    public enum ErrorType {
        GLOBAL,
        PROFILES_PAGE,
        PROFILE,
        ;
    }

    public interface AccountCallback {
        void processAccount(GameInfoResponse gameInfoResponse);
        void onError(ErrorType errorType, int id, String message);
        void onFinish();
    }

}
