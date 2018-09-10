/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.pulsar.flightpath.lib;

import com.hrznstudio.titanium.pulsar.flightpath.ISubscriberLocator;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of ISubscriberLocator
 *
 * @author Arkan <arkan@drakon.io>
 */
@ParametersAreNonnullByDefault
public class AnnotationLocator implements ISubscriberLocator {

    private final Class<? extends Annotation> annotation;

    public AnnotationLocator(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Nonnull
    @Override
    public Map<Class, Set<Method>> findSubscribers(Object obj) {
        Map<Class, Set<Method>> methods = new HashMap<Class, Set<Method>>();
        for (Method m : obj.getClass().getMethods()) {
            if (m.isAnnotationPresent(annotation) && m.getParameterTypes().length == 1) {
                Class param = m.getParameterTypes()[0];
                if (!methods.containsKey(param)) methods.put(param, new HashSet<Method>());
                methods.get(param).add(m);
            }
        }
        return methods;
    }

    @Nonnull
    @Override
    public Set<Pair<Object, Map<Class, Set<Method>>>> findSubscribers() {
        return new HashSet<Pair<Object, Map<Class, Set<Method>>>>();
    }
}
