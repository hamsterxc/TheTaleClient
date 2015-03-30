package com.lonebytesoft.thetaleclient.sdk.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hamster
 * @since 18.03.2015
 */
public class Cache {

    private static final Map<String, CacheItem> cache = new ConcurrentHashMap<>();

    public static synchronized String get(final String key, final long minTime) {
        final CacheItem cacheItem = cache.get(key);
        if((cacheItem != null) && (cacheItem.time >= minTime)) {
            return cacheItem.data;
        } else {
            return null;
        }
    }

    public static synchronized void complete(final String key, final String data, final long time) {
        cache.put(key, new CacheItem(data, time));
    }

}
