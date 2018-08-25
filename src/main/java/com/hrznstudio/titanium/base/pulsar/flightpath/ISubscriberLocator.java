/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.base.pulsar.flightpath;

import com.hrznstudio.titanium.base.pulsar.flightpath.lib.Pair;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Interface for defining custom subscriber scanners.
 *
 * @author Arkan <arkan@drakon.io>
 */
@ParametersAreNonnullByDefault
public interface ISubscriberLocator {

    /**
     * Find all subscription methods on the given object.
     *
     * @param obj Object to scan for subscribers
     * @return A map of event classes -> methods listening for them
     */
    @Nonnull
    Map<Class, Set<Method>> findSubscribers(Object obj);

    /**
     * Scan for any subscribers anywhere in the current JVM. This will often simply return an empty map. Implementations
     * should construct any required objects and return them to Flightpath along with their subscription data.
     *
     * @return A map of handlers to a map of event classes -> methods listening for them
     */
    @Nonnull
    Set<Pair<Object, Map<Class, Set<Method>>>> findSubscribers();

}
