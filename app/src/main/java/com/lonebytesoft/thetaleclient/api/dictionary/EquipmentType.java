package com.lonebytesoft.thetaleclient.api.dictionary;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum EquipmentType {

    MAIN_HAND(0, R.drawable.artifact_main_hand),
    OFF_HAND(1, R.drawable.artifact_off_hand),
    HEAD(2, R.drawable.artifact_head),
    AMULET(9, R.drawable.artifact_amulet),
    SHOULDERS(3, R.drawable.artifact_shoulders),
    BODY(4, R.drawable.artifact_body),
    GLOVES(5, R.drawable.artifact_gloves),
    CLOAK(6, R.drawable.artifact_cloak),
    TROUSERS(7, R.drawable.artifact_trousers),
    BOOTS(8, R.drawable.artifact_boots),
    RING(10, R.drawable.artifact_ring),
    ;

    private final int code;
    private final int drawableResId;

    private EquipmentType(final int code, final int drawableResId) {
        this.code = code;
        this.drawableResId = drawableResId;
    }

    public int getCode() {
        return code;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

}
