package com.lonebytesoft.thetaleclient.api.dictionary;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum CardRarity {

    COMMON(0, R.color.card_common),
    UNCOMMON(1, R.color.card_uncommon),
    RARE(2, R.color.card_rare),
    EPIC(3, R.color.card_epic),
    LEGENDARY(4, R.color.card_legendary),
    ;

    private final int code;
    private final int colorResId;

    CardRarity(final int code, final int colorResId) {
        this.code = code;
        this.colorResId = colorResId;
    }

    public int getCode() {
        return code;
    }

    public int getColorResId() {
        return colorResId;
    }

}
