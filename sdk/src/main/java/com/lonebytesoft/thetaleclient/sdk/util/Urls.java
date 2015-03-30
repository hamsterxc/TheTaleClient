package com.lonebytesoft.thetaleclient.sdk.util;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public interface Urls {

    String BASE_PROTOCOL = "http:";
    String BASE_DOMAIN = "the-tale.org";
    String BASE_API = BASE_PROTOCOL + "//" + BASE_DOMAIN + "/%s";

    String API_POSTPONED_TASK = BASE_API;

}
