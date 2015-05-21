package com.lonebytesoft.thetaleclient.desktop.module;

import com.lonebytesoft.thetaleclient.desktop.Utils;
import com.lonebytesoft.thetaleclient.sdk.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.exception.HttpException;
import com.lonebytesoft.thetaleclient.sdk.exception.UpdateException;
import com.lonebytesoft.thetaleclient.sdk.helper.MapHelper;
import com.lonebytesoft.thetaleclient.sdk.model.AccountShortInfo;
import com.lonebytesoft.thetaleclient.sdk.model.HeroInfo;
import com.lonebytesoft.thetaleclient.sdk.request.AccountsListRequest;
import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.InfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.MapRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;

import org.apache.http.util.TextUtils;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import javafx.util.Pair;

/**
 * @author Hamster
 * @since 28.04.2015
 */
public class HeroesMap {

    public static void execute() throws ApiException {
        final Map<Integer, String> periods = new HashMap<>();
        while(true) {
            Utils.consoleWrite("Heroes activity period in seconds to draw on map (-1 for no heroes, 0 for all heroes, empty line to finish):");
            final String period = Utils.consoleReadLine();
            if(TextUtils.isEmpty(period)) {
                break;
            } else {
                Utils.consoleWrite("Map image file name:");
                periods.put(Integer.decode(period), Utils.consoleReadLine());
            }
        }

        boolean shouldGetHeroes = false;
        final Map<Integer, Pair<AtomicInteger, List<HeroInfo>>> heroes =
                Collections.synchronizedMap(new HashMap<Integer, Pair<AtomicInteger, List<HeroInfo>>>());
        for(final Integer period : periods.keySet()) {
            if(period != -1) {
                shouldGetHeroes = true;
            }
            heroes.put(period, new Pair<>(new AtomicInteger(0), Collections.synchronizedList(new ArrayList<HeroInfo>())));
        }

        final long currentTime = System.currentTimeMillis() / 1000;
        if(shouldGetHeroes) {
            final ExecutorService executorService = Executors.newFixedThreadPool(10);
            final int pagesCount = new AccountsListRequest("", 0).execute().pagesCount;
            final AtomicInteger countPages = new AtomicInteger(0);
            for(int i = 1; i <= pagesCount; i++) {
                final int index = i;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final List<AccountShortInfo> accounts = new AccountsListRequest("", index).execute().accounts;
                            final int elementsCount = accounts.size();
                            for (int elementIndex = 0; elementIndex < elementsCount; elementIndex++) {
                                try {
                                    final GameInfoResponse gameInfoResponse = new GameInfoRequest(
                                            Utils.CLIENT_ID, accounts.get(elementIndex).id).execute();
                                    for (final Map.Entry<Integer, Pair<AtomicInteger, List<HeroInfo>>> entry : heroes.entrySet()) {
                                        if ((entry.getKey() == 0)
                                                || (gameInfoResponse.account.lastVisitTime + entry.getKey() > currentTime)) {
                                            entry.getValue().getKey().incrementAndGet();
                                            entry.getValue().getValue().add(gameInfoResponse.account.hero);
                                        }
                                    }
                                } catch (UpdateException e) {
                                    Utils.consoleWriteError("\rError: Updating");
                                    System.exit(0);
                                } catch (ApiException e) {
                                    if (e instanceof HttpException) {
                                        Utils.consoleWriteError(String.format("\rError: HTTP %d", ((HttpException) e).getHttpStatusCode()));
                                    } else {
                                        Utils.consoleWriteError("\r" + e.getMessage());
                                    }
                                    elementIndex--;
                                }
                            }
                            System.out.print(String.format("\r%d/%d pages", countPages.incrementAndGet(), pagesCount));
                        } catch (ApiException e) {
                            executorService.execute(this);
                        }
                    }
                });
            }

            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                Utils.consoleWrite("");
            } catch (InterruptedException e) {
                Utils.consoleWriteError("\n" + e.getMessage());
            }
        }

        final GameInfoResponse gameInfoResponse = new GameInfoRequest(Utils.CLIENT_ID).execute();
        final InfoResponse infoResponse = new InfoRequest(Utils.CLIENT_ID).execute();
        final MapResponse mapResponse = new MapRequest(infoResponse.dynamicContentUrl, gameInfoResponse.mapVersion).execute();
        final Point mapSize = MapHelper.getMapSize(mapResponse);

        for(final Map.Entry<Integer, Pair<AtomicInteger, List<HeroInfo>>> entry : heroes.entrySet()) {
            final BufferedImage mapImage = MapHelper.getMapImage(
                    mapSize.x, mapSize.y, MapStyle.STANDARD, true,
                    mapResponse, infoResponse.staticContentUrl, entry.getValue().getValue(), false);
            if(mapImage == null) {
                Utils.consoleWriteError("Could not get map_hour image");
            } else {
                try {
                    final String filename = periods.get(entry.getKey());
                    ImageIO.write(mapImage, "png", new File(filename));
                    Utils.consoleWrite(String.format("%s : %d heroes", filename, entry.getValue().getValue().size()));
                } catch (IOException e) {
                    Utils.consoleWriteError(e.getMessage());
                }
            }
        }

        Utils.consoleWrite(String.format("Done: %d s", System.currentTimeMillis() / 1000 - currentTime));
    }

}
