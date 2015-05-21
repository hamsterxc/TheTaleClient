package com.lonebytesoft.thetaleclient.desktop;

import com.lonebytesoft.thetaleclient.desktop.module.CardsTotalPrice;
import com.lonebytesoft.thetaleclient.desktop.module.HeroesMap;
import com.lonebytesoft.thetaleclient.desktop.module.Subscribers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Hamster
 * @since 28.04.2015
 */
public enum Module {

    CARDS_TOTAL_PRICE("Account cards total price", CardsTotalPrice.class),
    HEROES_MAP("Map with heroes", HeroesMap.class),
    SUBSCRIBERS("Subscribers", Subscribers.class),
    ;

    public final String name;
    private final Class<?> clazz;

    Module(final String name, final Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public void execute() throws Throwable {
        try {
            final Method method = clazz.getMethod("execute");
            method.invoke(null);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(String.format("Could not execute module %s", name));
        }
    }

}
