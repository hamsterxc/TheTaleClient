package com.lonebytesoft.thetaleclient.sdk;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public enum ApiResponseStatus {

    OK("ok"),
    ERROR("error"),
    PROCESSING("processing"),
    GENERIC("generic"),
    ;

    public final String code;
    public final String name;

    ApiResponseStatus(final String code) {
        this.code = code;
        this.name = code;
    }

}
