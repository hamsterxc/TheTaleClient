package com.lonebytesoft.thetaleclient.api.dictionary;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public enum ArtifactRarity {

    COMMON(0, R.color.artifact_common, false, "обычный артефакт"),
    RARE(1, R.color.artifact_rare, true, "редкий артефакт"),
    EPIC(2, R.color.artifact_epic, true, "эпический артефакт"),
    ;

    private final int code;
    private final int colorResId;
    private final boolean isExceptional;
    private final String name;

    private ArtifactRarity(final int code, final int colorResId, final boolean isExceptional, final String name) {
        this.code = code;
        this.colorResId = colorResId;
        this.isExceptional = isExceptional;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public int getColorResId() {
        return colorResId;
    }

    public boolean isExceptional() {
        return isExceptional;
    }

    public String getName() {
        return name;
    }

}
