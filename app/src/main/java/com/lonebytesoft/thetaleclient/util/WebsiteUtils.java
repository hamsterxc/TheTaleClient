package com.lonebytesoft.thetaleclient.util;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.model.ArtifactBaseInfo;
import com.lonebytesoft.thetaleclient.api.model.MonsterBaseInfo;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Hamster
 * @since 17.01.2015
 * TODO rewrite enumAccounts & enumMonsters using ExecutorService
 * TODO rework urls: use RequestUtils.URL_BASE, move urls to a separate static class
 */
public class WebsiteUtils {

    private static final int THREADS_COUNT = 20;

    public static final String URL_GAME = "http://the-tale.org/game/?action=the-tale-client";
    public static final String URL_PROFILE_KEEPER = "http://the-tale.org/accounts/%d?action=the-tale-client";
    public static final String URL_PROFILE_HERO = "http://the-tale.org/game/heroes/%d?action=the-tale-client";

    private static final String PAGE_ACCOUNTS = "http://the-tale.org/accounts/";
    private static final String PAGE_MONSTERS = "http://the-tale.org/guide/mobs/";
    private static final String PAGE_MONSTER = "http://the-tale.org/guide/mobs/%d/info";
    private static final String PAGE_ARTIFACT = "http://the-tale.org/guide/artifacts/%d/info";

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

                final Collection<Integer> accounts = new ArrayList<>();
                final Queue<Integer> pages = new LinkedList<>();
                for(int i = 1; i <= pagesCount; i++) {
                    pages.add(i);
                }

                final CountDownLatch latch = new CountDownLatch(1);
                getPageAccounts(pages, accounts, latch, callback);
                try {
                    latch.await();
                } catch(InterruptedException e) {
                    callback.onError(ErrorType.GLOBAL, 0, e.getMessage());
                    return;
                }

                enumAccounts(callback, accounts);
            }
        }).start();
    }

    /**
     * Enums specified game accounts using provided callback
     */
    public static void enumAccounts(final AccountCallback callback, final Collection<Integer> accounts) {
        final Queue<Integer> accountsQueue = new LinkedList<>(accounts);
        for(int i = 0; i < THREADS_COUNT; i++) {
            processAccounts(accountsQueue, callback);
        }
    }

    private static void getPageAccounts(final Queue<Integer> pages, final Collection<Integer> accounts,
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
                        accounts.add(getIdFromUrl(element.children().get(0).children().get(0).attr("href")));
                    }
                } catch(IOException e) {
                    callback.onError(ErrorType.ITEMS_LIST, id, e.getMessage());
                }
                getPageAccounts(pages, accounts, latch, callback);
            }
        }).start();
    }

    private static void processAccounts(final Queue<Integer> accounts, final AccountCallback callback) {
        final Integer id;
        final Integer next;
        synchronized(lock) {
            id = accounts.poll();
            next = accounts.peek();
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
                    processAccounts(accounts, callback);
                }
            }

            @Override
            public void processError(GameInfoResponse response) {
                if(!"game.info.account.not_found".equals(response.errorCode)) {
                    callback.onError(ErrorType.ITEM, id, response.errorMessage);
                }
                if(next == null) {
                    callback.onFinish();
                } else {
                    processAccounts(accounts, callback);
                }
            }
        }, false);
    }

    /**
     * Enums all game monsters using provided callback
     * Uses HTML parsing from http://the-tale.org/guide/mobs/
     */
    public static void enumMonsters(final MonsterCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Queue<Integer> monsterIds = new LinkedList<>();
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpRequest httpRequest = HttpMethod.GET.getHttpRequest(PAGE_MONSTERS, null, null);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();

                    final Document document = Jsoup.parse(outputStream.toString());
                    final Elements elements = document.select("table tr > td:nth-child(2) > a");
                    for(final Element element : elements) {
                        monsterIds.add(getIdFromUrl(element.attr("href")));
                    }
                } catch(IOException e) {
                    callback.onError(ErrorType.ITEMS_LIST, 0, e.getMessage());
                    return;
                }

                for(int i = 0; i < THREADS_COUNT; i++) {
                    processMonsters(monsterIds, callback);
                }
            }
        }).start();
    }

    private static void processMonsters(final Queue<Integer> monsterIds, final MonsterCallback callback) {
        final Integer id;
        final Integer next;
        synchronized(lock) {
            id = monsterIds.poll();
            next = monsterIds.peek();
        }
        if(id == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpRequest httpRequest = HttpMethod.GET.getHttpRequest(String.format(PAGE_MONSTER, id), null, null);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();

                    final Document document = Jsoup.parse(outputStream.toString());
                    final Elements elements = document.select("table tr > td");

                    final List<ArtifactBaseInfo> artifactsList = new ArrayList<>();
                    final Elements artifacts = elements.get(5).select("a");
                    for(final Element artifact : artifacts) {
                        final ArtifactBaseInfo artifactBaseInfo = getArtifactBaseInfo(getIdFromUrl(artifact.attr("href")), callback);
                        if(artifactBaseInfo != null) {
                            artifactsList.add(artifactBaseInfo);
                        }
                    }

                    final List<ArtifactBaseInfo> junkList = new ArrayList<>();
                    final Elements junk = elements.get(6).select("a");
                    for(final Element junkItem : junk) {
                        final ArtifactBaseInfo artifactBaseInfo = getArtifactBaseInfo(getIdFromUrl(junkItem.attr("href")), callback);
                        if(artifactBaseInfo != null) {
                            junkList.add(artifactBaseInfo);
                        }
                    }

                    final Element content = document.select("div.pgf-scrollable").first().clone();
                    content.child(0).remove();

                    callback.processMonster(new MonsterBaseInfo(
                            document.select(".dialog-title").first().text(),
                            elements.get(0).text(),
                            elements.get(1).text(),
                            elements.get(2).text(),
                            elements.get(3).text(),
                            elements.get(4).text(),
                            artifactsList,
                            junkList,
                            content.html()
                    ));

                    if(next == null) {
                        callback.onFinish();
                    } else {
                        processMonsters(monsterIds, callback);
                    }
                } catch(IOException e) {
                    callback.onError(ErrorType.ITEM, id, e.getMessage());
                    if(next == null) {
                        callback.onFinish();
                    } else {
                        processMonsters(monsterIds, callback);
                    }
                }
            }
        }).start();
    }

    private static ArtifactBaseInfo getArtifactBaseInfo(final int artifactId, final MonsterCallback callback) {
        final HttpClient httpClient = new DefaultHttpClient();
        final HttpRequest httpRequest = HttpMethod.GET.getHttpRequest(String.format(PAGE_ARTIFACT, artifactId), null, null);
        try {
            final OutputStream outputStream = new ByteArrayOutputStream();
            httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
            outputStream.close();

            final Document document = Jsoup.parse(outputStream.toString());
            final Elements elements = document.select("table tr > td");

            final Element content = document.select("div.pgf-scrollable").first().clone();
            content.child(0).remove();

            return new ArtifactBaseInfo(
                    document.select(".dialog-title").first().text(),
                    elements.get(0).text(),
                    elements.get(2).text(),
                    elements.get(3).text(),
                    elements.get(4).text(),
                    elements.get(5).text(),
                    elements.get(6).text(),
                    content.html()
            );
        } catch(IOException e) {
            callback.onError(ErrorType.SUBITEM, artifactId, e.getMessage());
            return null;
        }
    }

    private static int getIdFromUrl(final String url) {
        return Integer.parseInt(url.substring(url.lastIndexOf('/') + 1));
    }

    public static void enumAccountPages(final String prefix, final AccountPageCallback callback) {
        final String accountPrefix = prefix == null ? "" : prefix;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int pagesCount;
                final HttpMethod httpMethod = HttpMethod.GET;
                final Map<String, String> getParams = new HashMap<>(1);
                getParams.put("prefix", accountPrefix);
                final HttpClient httpClient = new DefaultHttpClient();
                final HttpRequest httpRequest = httpMethod.getHttpRequest(PAGE_ACCOUNTS, getParams, null);
                try {
                    final OutputStream outputStream = new ByteArrayOutputStream();
                    httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                    outputStream.close();

                    final Document document = Jsoup.parse(outputStream.toString());
                    final Elements elements = document.select("div.pagination");

                    if(elements.size() == 0) {
                        callback.onError(ErrorType.ITEMS_LIST, 0, TheTaleClientApplication.getContext().getString(R.string.find_player_not_found));
                        callback.onFinish();
                        return;
                    }

                    final Elements pageLinks = elements.get(0).children().get(0).children();
                    pagesCount = Integer.parseInt(pageLinks.get(pageLinks.size() - 1).child(0).ownText());
                } catch(IOException e) {
                    callback.onError(ErrorType.GLOBAL, 0, e.getMessage());
                    return;
                }

                if(!callback.processPagesCount(pagesCount)) {
                    callback.onFinish();
                    return;
                }

                final ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
                for(int i = 1; i <= pagesCount; i++) {
                    final int id = i;
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            final HttpMethod httpMethod = HttpMethod.GET;
                            final Map<String, String> getParams = new HashMap<>(2);
                            getParams.put("prefix", prefix);
                            getParams.put("page", String.valueOf(id));
                            final HttpClient httpClient = new DefaultHttpClient();
                            final HttpRequest httpRequest = httpMethod.getHttpRequest(PAGE_ACCOUNTS, getParams, null);

                            final Map<Integer, String> accounts = new HashMap<>();
                            try {
                                final OutputStream outputStream = new ByteArrayOutputStream();
                                httpClient.execute((HttpUriRequest) httpRequest).getEntity().writeTo(outputStream);
                                outputStream.close();

                                final Document document = Jsoup.parse(outputStream.toString());
                                final Elements elements = document.select("tr.pgf-account-record");
                                for(final Element element : elements) {
                                    final Element linkElement = element.children().get(0).children().get(0);
                                    accounts.put(getIdFromUrl(linkElement.attr("href")), linkElement.ownText());
                                }

                                callback.processAccounts(accounts);
                            } catch(IOException e) {
                                callback.onError(ErrorType.ITEM, id, e.getMessage());
                            }
                        }
                    });
                }

                executorService.shutdown();
                try {
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    callback.onError(ErrorType.GLOBAL, 0, e.getMessage());
                    return;
                }
                callback.onFinish();
            }
        }).start();
    }

    public enum ErrorType {
        GLOBAL,
        ITEMS_LIST,
        ITEM,
        SUBITEM,
        ;
    }

    private interface EnumCallback {
        void onError(ErrorType errorType, int id, String message);
        void onFinish();
    }

    public interface AccountCallback extends EnumCallback {
        void processAccount(GameInfoResponse gameInfoResponse);
    }

    public interface MonsterCallback extends EnumCallback {
        void processMonster(MonsterBaseInfo monster);
    }

    public interface AccountPageCallback extends EnumCallback {
        boolean processPagesCount(int count);
        void processAccounts(Map<Integer, String> accounts);
    }

}
