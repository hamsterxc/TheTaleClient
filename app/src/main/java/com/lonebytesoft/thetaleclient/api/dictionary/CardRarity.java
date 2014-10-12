package com.lonebytesoft.thetaleclient.api.dictionary;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum CardRarity {

    COMMON(R.color.card_common),
    UNCOMMON(R.color.card_uncommon),
    RARE(R.color.card_rare),
    EPIC(R.color.card_epic),
    LEGENDARY(R.color.card_legendary),
    ;

    private final int colorResId;

    private CardRarity(final int colorResId) {
        this.colorResId = colorResId;
    }

    public int getColorResId() {
        return colorResId;
    }

}
