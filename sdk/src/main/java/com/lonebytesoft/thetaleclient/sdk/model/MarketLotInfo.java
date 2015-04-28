package com.lonebytesoft.thetaleclient.sdk.model;

import com.lonebytesoft.thetaleclient.sdk.dictionary.CardType;
import com.lonebytesoft.thetaleclient.sdk.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hamster
 * @since 27.04.2015
 */
public class MarketLotInfo {

    public final CardType type;
    public final String name;
    public final int price;
    public final long time;

    public MarketLotInfo(final JSONObject json) throws JSONException {
        type = ObjectUtils.getEnumForCode(CardType.class, json.getInt("type"));
        name = json.getString("name");
        price = json.getInt("price");
        time = json.getLong("time");
    }

    public MarketLotInfo(final String cardName, final String price, final String time) {
        this.name = cardName;
        CardType type = ObjectUtils.getEnumForName(CardType.class, cardName);
        if(type == null) { // companions
            for(final CardType cardType : CardType.values()) {
                if(cardName.startsWith(cardType.name)) {
                    type = cardType;
                    break;
                }
            }
            if(type == null) {
                throw new IllegalArgumentException(String.format("Invalid card name: %s - card not found", cardName));
            }
        }
        this.type = type;

        try {
            this.price = Integer.decode(price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Invalid price: %s - %s", price, e.getMessage()));
        }

        final Pattern pattern = Pattern.compile("(\\d+)\\s+(.+)");
        final Matcher matcher = pattern.matcher(time);
        if(matcher.find()) {
            try {
                switch(matcher.group(2)) {
                    case "день":
                    case "дня":
                    case "дней":
                        this.time = Integer.decode(matcher.group(1)) * 24 * 60 * 60;
                        break;

                    case "час":
                    case "часа":
                    case "часов":
                        this.time = Integer.decode(matcher.group(1)) * 60 * 60;
                        break;

                    case "минута":
                    case "минуты":
                    case "минут":
                        this.time = Integer.decode(matcher.group(1)) * 60;
                        break;

                    default:
                        throw new IllegalArgumentException(String.format("Invalid time: %s - unknown duration unit", time));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("Invalid time: %s - could not parse duration, %s", time, e.getMessage()));
            }
        } else {
            throw new IllegalArgumentException(String.format("Invalid time: %s - could not parse", time));
        }
    }

}
