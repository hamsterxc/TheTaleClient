package com.lonebytesoft.thetaleclient.desktop.module;

import com.lonebytesoft.thetaleclient.desktop.Utils;
import com.lonebytesoft.thetaleclient.sdk.exception.ApiException;
import com.lonebytesoft.thetaleclient.sdk.model.CardInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MarketLotInfo;
import com.lonebytesoft.thetaleclient.sdk.request.AuthRequest;
import com.lonebytesoft.thetaleclient.sdk.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.sdk.request.MarketRequest;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MarketResponse;

/**
 * @author Hamster
 * @since 28.04.2015
 */
public class CardsTotalPrice {

    public static void execute() throws ApiException {
        new AuthRequest(Utils.CLIENT_ID,
                Utils.consoleReadLine("E-mail: "),
                Utils.consoleReadPassword("Password: "))
                .execute();

        final MarketResponse marketResponse = new MarketRequest().execute();
        final GameInfoResponse gameInfoResponse = new GameInfoRequest(Utils.CLIENT_ID).execute();
        double total = 0;
        for(final CardInfo cardInfo : gameInfoResponse.account.hero.cards.cards) {
            final double price = getPrice(cardInfo, marketResponse);
            if(price == 0) {
                Utils.consoleWriteError(String.format("Unknown price for card %s", cardInfo.name));
            } else {
                total += price;
            }
        }
        Utils.consoleWrite(String.format("Total cards price: %f", total));
    }

    private static double getPrice(final CardInfo cardInfo, final MarketResponse marketResponse) {
        double cardPrice = 0;
        int cardCount = 0;
        double typePrice = 0;
        int typeCount = 0;
        double rarityPrice = 0;
        int rarityCount = 0;
        for(final MarketLotInfo marketLotInfo : marketResponse.lots) {
            if(cardInfo.rarity == marketLotInfo.type.rarity) {
                rarityPrice += marketLotInfo.price;
                rarityCount++;
            }
            if(cardInfo.type == marketLotInfo.type) {
                typePrice += marketLotInfo.price;
                typeCount++;
            }
            if(cardInfo.name.equals(marketLotInfo.name)) {
                cardPrice += marketLotInfo.price;
                cardCount++;
            }
        }

        if(cardCount == 0) {
            if(typeCount == 0) {
                if(rarityCount == 0) {
                    return 0;
                } else {
                    return rarityPrice / rarityCount;
                }
            } else {
                return typePrice / typeCount;
            }
        } else {
            return cardPrice / cardCount;
        }
    }

}
