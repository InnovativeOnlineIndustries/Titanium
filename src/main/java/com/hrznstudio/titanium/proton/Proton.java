/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.proton;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.proton.control.ProtonManager;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Method;
import java.util.List;

public abstract class Proton {
    private final ProtonManager manager;
    private final ProtonData data = getClass().getAnnotation(ProtonData.class);
    private boolean active;

    public Proton(ProtonManager manager) {
        this.manager = manager;
    }

    protected <T extends IForgeRegistryEntry<T>> void addEntry(Class<T> tClass, T t) {
        manager.addEntry(tClass, t);
    }

    protected <T extends IForgeRegistryEntry<T>> void addEntries(Class<T> tClass, T... ts) {
        for (T t : ts)
            addEntry(tClass, t);
    }

    public final List<Method> getEventMethods() {
        ImmutableList.Builder<Method> builder = new ImmutableList.Builder<>();
        Class clazz = getClass();
        while (clazz != null) {
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventReceiver.class)) {
                    if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        builder.add(method);
                    }
                }
            }
            clazz = null;
        }
        return builder.build();
    }

    public abstract void init();

    public ProtonData getData() {
        return data;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}