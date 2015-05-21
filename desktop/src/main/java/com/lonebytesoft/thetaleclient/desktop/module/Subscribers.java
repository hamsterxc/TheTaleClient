package com.lonebytesoft.thetaleclient.desktop.module;

import com.lonebytesoft.thetaleclient.desktop.Utils;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.exception.HttpException;
import com.lonebytesoft.thetaleclient.sdk.exception.UpdateException;
import com.lonebytesoft.thetaleclient.sdk.model.AccountPlaceHistoryInfo;
import com.lonebytesoft.thetaleclient.sdk.model.AccountShortInfo;
import com.lonebytesoft.thetaleclient.sdk.request.AccountInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.AccountsListRequest;
import com.lonebytesoft.thetaleclient.sdk.response.AccountInfoResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;

/**
 * @author Hamster
 * @since 12.05.2015
 */
public class Subscribers {

    public static void execute() throws ApiException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final int pagesCount = new AccountsListRequest("", 0).execute().pagesCount;
        final AtomicInteger countPages = new AtomicInteger(0);

        final Map<String, String> clans = new ConcurrentHashMap<>(); // abbreviation -> full name
        final Map<String, List<String>> clanMembers = new ConcurrentHashMap<>(); // abbreviation -> account names
        final Map<String, Map<String, List<String>>> placeClanMembers =
                new ConcurrentHashMap<>(); // place name -> (abbreviation -> account names)

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
                                final AccountInfoResponse accountInfoResponse = new AccountInfoRequest(
                                        Utils.CLIENT_ID, accounts.get(elementIndex).id).execute();
                                if (accountInfoResponse.canAffectGame) {
                                    final Pair<String, String> clan = getClan(accountInfoResponse.accountId);
                                    if (!clans.containsKey(clan.getKey())) {
                                        clans.put(clan.getKey(), clan.getValue());
                                        clanMembers.put(clan.getKey(), new CopyOnWriteArrayList<String>());
                                    }
                                    clanMembers.get(clan.getKey()).add(accountInfoResponse.name);

                                    final int size = accountInfoResponse.placesHistory.size();
                                    if (size > 0) {
                                        Collections.sort(accountInfoResponse.placesHistory, new Comparator<AccountPlaceHistoryInfo>() {
                                            @Override
                                            public int compare(AccountPlaceHistoryInfo lhs, AccountPlaceHistoryInfo rhs) {
                                                if (lhs.helpCount == rhs.helpCount) {
                                                    return lhs.name.compareToIgnoreCase(rhs.name);
                                                } else {
                                                    return rhs.helpCount - lhs.helpCount;
                                                }
                                            }
                                        });
                                        int lastCount = accountInfoResponse.placesHistory.get(0).helpCount;
                                        for (int i = 0; i < size; i++) {
                                            final AccountPlaceHistoryInfo accountPlaceHistoryInfo = accountInfoResponse.placesHistory.get(i);
                                            if ((i >= 10) && (accountPlaceHistoryInfo.helpCount < lastCount)) {
                                                break;
                                            } else {
                                                lastCount = accountPlaceHistoryInfo.helpCount;
                                            }

                                            if (!placeClanMembers.containsKey(accountPlaceHistoryInfo.name)) {
                                                placeClanMembers.put(accountPlaceHistoryInfo.name, new ConcurrentHashMap<String, List<String>>());
                                            }
                                            if (!placeClanMembers.get(accountPlaceHistoryInfo.name).containsKey(clan.getKey())) {
                                                placeClanMembers.get(accountPlaceHistoryInfo.name).put(clan.getKey(), new CopyOnWriteArrayList<String>());
                                            }
                                            placeClanMembers.get(accountPlaceHistoryInfo.name).get(clan.getKey()).add(accountInfoResponse.name);
                                        }
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

        Utils.consoleWrite("");
        final List<Pair<String, List<String>>> clanMembersList = new ArrayList<>(clanMembers.size());
        int total = 0;
        for(final Map.Entry<String, List<String>> clanMembersEntry : clanMembers.entrySet()) {
            clanMembersList.add(new Pair<>(clanMembersEntry.getKey(), clanMembersEntry.getValue()));
            total += clanMembersEntry.getValue().size();
        }
        Collections.sort(clanMembersList, new Comparator<Pair<String, List<String>>>() {
            @Override
            public int compare(Pair<String, List<String>> o1, Pair<String, List<String>> o2) {
                return clans.get(o1.getKey()).compareTo(clans.get(o2.getKey()));
            }
        });
        Utils.consoleWrite(String.format("Total count : %d", total));
        for(final Pair<String, List<String>> clanMembersEntry : clanMembersList) {
            Utils.consoleWrite(String.format("[%s] %s : %d", clanMembersEntry.getKey(),
                    clans.get(clanMembersEntry.getKey()), clanMembersEntry.getValue().size()));
        }

        Utils.consoleWrite("");
        final List<Pair<String, List<Pair<String, List<String>>>>> placeClanMembersList = new ArrayList<>(placeClanMembers.size());
        final Map<String, Integer> placeCount = new HashMap<>(placeClanMembers.size());
        for(Map.Entry<String, Map<String, List<String>>> placeClanMembersEntry : placeClanMembers.entrySet()) {
            final List<Pair<String, List<String>>> membersList = new ArrayList<>(placeClanMembersEntry.getValue().size());
            int count = 0;
            for(final Map.Entry<String, List<String>> members : placeClanMembersEntry.getValue().entrySet()) {
                membersList.add(new Pair<>(members.getKey(), members.getValue()));
                count += members.getValue().size();
            }
            Collections.sort(membersList, new Comparator<Pair<String, List<String>>>() {
                @Override
                public int compare(Pair<String, List<String>> o1, Pair<String, List<String>> o2) {
                    return clans.get(o1.getKey()).compareTo(clans.get(o2.getKey()));
                }
            });
            placeClanMembersList.add(new Pair<>(placeClanMembersEntry.getKey(), membersList));
            placeCount.put(placeClanMembersEntry.getKey(), count);
        }
        Collections.sort(placeClanMembersList, new Comparator<Pair<String, List<Pair<String, List<String>>>>>() {
            @Override
            public int compare(Pair<String, List<Pair<String, List<String>>>> o1, Pair<String, List<Pair<String, List<String>>>> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        for(Pair<String, List<Pair<String, List<String>>>> placeClanMembersEntry : placeClanMembersList) {
            Utils.consoleWrite(String.format("%s (Total count : %d)",
                    placeClanMembersEntry.getKey(), placeCount.get(placeClanMembersEntry.getKey())));
            for(final Pair<String, List<String>> members : placeClanMembersEntry.getValue()) {
                Utils.consoleWrite(String.format("\t[%s] %s : %d", members.getKey(),
                        clans.get(members.getKey()), members.getValue().size()));
            }
        }
    }

    private static Pair<String, String> getClan(final int accountId) {
        final Document document = Jsoup.parse(Utils.getUrl(
                String.format("http://the-tale.org/accounts/%d", accountId)));
        final String text = document.select("ul.game-navigation li").get(3).text();
        final Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]\\s+(.*)");
        final Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
            return new Pair<>(matcher.group(1), matcher.group(2));
        } else {
            return new Pair<>("", "(no guild)");
        }
    }

}
