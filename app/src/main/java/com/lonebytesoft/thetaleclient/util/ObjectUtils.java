package com.lonebytesoft.thetaleclient.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 02.10.2014
 */
public class ObjectUtils {

    private static final String METHOD_GET_CODE = "getCode";
    private static final String METHOD_GET_NAME = "getName";

    private static final Map<Class<?>, Map<?, ?>> codeToEnumCache = new HashMap<>();
    private static final Map<Class<?>, Map<?, ?>> nameToEnumCache = new HashMap<>();
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
                final Method getCodeMethod = clazz.getMethod(METHOD_GET_CODE);
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

    public static <E extends Enum<E>, T> E getEnumForName(final Class<E> clazz, final T name) {
        if(!clazz.isEnum()) {
            return null;
        }

        Map<T, E> cache = (Map<T, E>) nameToEnumCache.get(clazz);
        if(cache == null) {
            try {
                final Method getNameMethod = clazz.getMethod(METHOD_GET_NAME);
                cache = new HashMap<>();
                for(final E enumEntry : clazz.getEnumConstants()) {
                    cache.put((T) getNameMethod.invoke(enumEntry), enumEntry);
                }
                nameToEnumCache.put(clazz, cache);
            } catch(NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
                return null;
            }
        }
        return cache.get(name);
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

    public static Integer getOptionalInteger(final JSONObject json, final String key) {
        if((json == null) || (key == null)) {
            return null;
        }

        try {
            if (json.isNull(key)) {
                return null;
            } else {
                return json.getInt(key);
            }
        } catch(JSONException e) {
            return null;
        }
    }

    public static int getOptionalInteger(final JSONObject json, final String key, final int defaultValue) {
        final Integer value = getOptionalInteger(json, key);
        return value == null ? defaultValue : value;
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
                final Method getNameMethod = clazz.getMethod(METHOD_GET_NAME);
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

    public static <T> Map<T, Integer> getItemsCountList(final Collection<T> items, final Comparator<T> comparator) {
        final List<T> list = new ArrayList<>(items);
        Collections.sort(list, comparator);

        T previous = null;
        final Map<T, Integer> countList = new LinkedHashMap<>();
        for(final T item : list) {
            if((previous == null) || (comparator.compare(previous, item) != 0)) {
                previous = item;
                countList.put(item, 1);
            } else {
                countList.put(previous, countList.get(previous) + 1);
            }
        }

        return countList;
    }

}
