package com.lonebytesoft.thetaleclient.sdk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class ObjectUtils {

    private static final String FIELD_CODE = "code";

    private static final Map<Class<?>, Map<?, ?>> codeToEnumCache = new HashMap<>();

    public static <E extends Enum<E>, T> E getEnumForCode(final Class<E> clazz, final T code) {
        if(!clazz.isEnum()) {
            return null;
        }

        Map<T, E> cache = (Map<T, E>) codeToEnumCache.get(clazz);
        if(cache == null) {
            try {
                final Field fieldCode = clazz.getField(FIELD_CODE);
                cache = new HashMap<>();
                for(final E enumEntry : clazz.getEnumConstants()) {
                    cache.put((T) fieldCode.get(enumEntry), enumEntry);
                }
                codeToEnumCache.put(clazz, cache);
            } catch(IllegalAccessException|NoSuchFieldException e) {
                return null;
            }
        }
        return cache.get(code);
    }

}
