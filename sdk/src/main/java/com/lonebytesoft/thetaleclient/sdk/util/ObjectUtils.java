package com.lonebytesoft.thetaleclient.sdk.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 13.03.2015
 */
public class ObjectUtils {

    private static final String FIELD_CODE = "code";
    private static final String FIELD_NAME = "name";

    private static final Map<Class<?>, Map<?, ?>> codeToEnumCache = new HashMap<>();
    private static final Map<Class<?>, Map<?, ?>> nameToEnumCache = new HashMap<>();

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

    public static <E extends Enum<E>, T> E getEnumForName(final Class<E> clazz, final T name) {
        if(!clazz.isEnum()) {
            return null;
        }

        Map<T, E> cache = (Map<T, E>) nameToEnumCache.get(clazz);
        if(cache == null) {
            try {
                final Field fieldName = clazz.getField(FIELD_NAME);
                cache = new HashMap<>();
                for(final E enumEntry : clazz.getEnumConstants()) {
                    cache.put((T) fieldName.get(enumEntry), enumEntry);
                }
                nameToEnumCache.put(clazz, cache);
            } catch(IllegalAccessException|NoSuchFieldException e) {
                return null;
            }
        }
        return cache.get(name);
    }

    public static <T> T getModelFromJson(final Class<T> clazz, final JSONObject json) {
        if(json == null) {
            return null;
        }

        try {
            return clazz.getConstructor(JSONObject.class).newInstance(json);
        } catch(NoSuchMethodException|InstantiationException|IllegalAccessException|InvocationTargetException e) {
            return null;
        }
    }

    public static String getOptionalString(final JSONObject json, final String key) {
        if((json == null) || (key == null)) {
            return null;
        }

        try {
            if (json.isNull(key)) {
                return null;
            } else {
                return json.getString(key);
            }
        } catch(JSONException e) {
            return null;
        }
    }

    public static JSONObject getObjectFromArray(final JSONArray jsonArray, final String[] names) {
        if((jsonArray == null) || (names == null)) {
            return null;
        }

        try {
            final JSONObject result = new JSONObject();
            final int count = Math.min(jsonArray.length(), names.length);
            for (int i = 0; i < count; i++) {
                result.put(names[i], jsonArray.get(i));
            }

            return result;
        } catch(JSONException e) {
            return null;
        }
    }

}
