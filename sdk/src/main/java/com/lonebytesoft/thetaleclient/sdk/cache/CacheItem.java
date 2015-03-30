package com.lonebytesoft.thetaleclient.sdk.cache;

/**
 * @author Hamster
 * @since 15.03.2015
 */
public class CacheItem {

    public String data;
    public long time;

    public CacheItem(final String data, final long time) {
        this.data = data;
        this.time = time;
    }

}
