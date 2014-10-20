package com.lonebytesoft.thetaleclient.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class ObjectUtils {

    private static final Map<Class<?>, Map<?, ?>> codeToEnumCache = new HashMap<>();
    private static final Map<Class<?>, String[]> enumToNamesCache = new HashMap<>();

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

    public static <E extends Enum<E>, T> E getEnumForCode(final Class<E> clazz, final T code) {
        if(!clazz.isEnum()) {
            return null;
        }

        Map<T, E> cache = (Map<T, E>) codeToEnumCache.get(clazz);
        if(cache == null) {
            try {
                final Method getCodeMethod = clazz.getMethod("getCode");
                cache = new HashMap<>();
                for(final E enumEntry : clazz.getEnumConstants()) {
                    cache.put((T) getCodeMethod.invoke(enumEntry), enumEntry);
                }
                codeToEnumCache.put(clazz, cache);
            } catch(NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
                return null;
            }
        }
        return cache.get(code);
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
        }catch(JSONException e) {
            return null;
        }
    }

    public static <E extends Enum<E>> String[] getNamesForEnum(final Class<E> clazz) {
        if(!clazz.isEnum()) {
            return null;
        }

        String[] names = enumToNamesCache.get(clazz);
        if(names == null) {
            try {
                final int count = clazz.getEnumConstants().length;
                names = new String[count];
                final Method getNameMethod = clazz.getMethod("getName");
                for(int i = 0; i < count; i++) {
                    names[i] = (String) getNameMethod.invoke(clazz.getEnumConstants()[i]);
                }
                enumToNamesCache.put(clazz, names);
            } catch(NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
                return null;
            }
        }
        return names;
    }

}
