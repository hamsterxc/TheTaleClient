package com.lonebytesoft.thetaleclient.api;

/**
 * @author Hamster
 * @since 01.10.2014
 */
public enum ApiResponseStatus {

    OK("ok"),
    ERROR("error"),
    PROCESSING("processing"),
    GENERIC("generic"),
    ;

    private final String code;

    private ApiResponseStatus(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
