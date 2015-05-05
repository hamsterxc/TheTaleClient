package com.lonebytesoft.thetaleclient.api.dictionary;

/**
 * @author Hamster
 * @since 05.05.2015
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

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
