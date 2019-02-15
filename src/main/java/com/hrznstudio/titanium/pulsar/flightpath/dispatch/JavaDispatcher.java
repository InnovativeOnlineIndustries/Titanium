/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.pulsar.flightpath.dispatch;

import com.hrznstudio.titanium.pulsar.flightpath.IDispatcher;
import com.hrznstudio.titanium.pulsar.flightpath.IExceptionHandler;
import com.hrznstudio.titanium.pulsar.flightpath.lib.Pair;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Basic reflection-based dispatcher.
 */
public class JavaDispatcher extends IDispatcher {

    private Map<Class, List<Pair<Object, Set<Method>>>> registeredListeners = new HashMap<Class, List<Pair<Object, Set<Method>>>>();

    public JavaDispatcher(IExceptionHandler exceptionHandler) {
        super(exceptionHandler);
    }

    @Override
    public void addSubscriber(@Nonnull Object target, @Nonnull Map<Class, Set<Method>> locatedMethods) {
        synchronized (lock) {
            for (Map.Entry<Class, Set<Method>> entry : locatedMethods.entrySet()) {
                List<Pair<Object, Set<Method>>> current = registeredListeners.get(entry.getKey());
                if (current == null) current = new ArrayList<Pair<Object, Set<Method>>>();
                current.add(new Pair<Object, Set<Method>>(target, entry.getValue()));
                registeredListeners.put(entry.getKey(), current);
            }
        }
    }

    @Override
    public void dispatch(@Nonnull Object evt) {
        synchronized (lock) {
            List<Pair<Object, Set<Method>>> listeners = registeredListeners.get(evt.getClass());
            if (listeners == null) return; // No listeners for type
            for (Pair<Object, Set<Method>> pair : listeners) {
                try {
                    Object target = pair.a;
                    Set<Method> methods = pair.b;
                    for (Method m : methods) {
                        boolean access = m.isAccessible();
                        m.setAccessible(true);
                        m.invoke(target, evt);
                        m.setAccessible(access);
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

}
