package com.lonebytesoft.thetaleclient.sdk.dictionary;

/**
 * @author Hamster
 * @since 06.05.2015
 */
public enum SocialLink {

    FRIEND(0, "партнёр"),
    ENEMY(1, "конкурент"),
    ;

    public final int code;
    public final String name;

    SocialLink(final int code, final String name) {
        this.code = code;
        this.name = name;
    }

}
