/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.proton;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.proton.api.IAlternativeEntries;
import com.hrznstudio.titanium.proton.api.RegistryManager;
import com.hrznstudio.titanium.proton.control.ProtonManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Proton implements RegistryManager {
    private final ProtonManager manager;
    private final ProtonData data = getClass().getAnnotation(ProtonData.class);
    private boolean active;
    private final Map<Class<? extends IForgeRegistryEntry>, List<?>> entries;
    private List<ResourceLocation> disabledEntries;
    private ItemGroup itemGroup;

    public Proton(ProtonManager manager) {
        this.manager = manager;
        this.disabledEntries = new ArrayList<>();
        this.entries = new HashMap<>();
        this.itemGroup = ItemGroup.MISC;
    }

    public final <T extends IForgeRegistryEntry<T>> List<T> getEntries(Class<T> tClass) {
        if (!entries.containsKey(tClass)) {
            entries.put(tClass, new ArrayList<T>());
        }
        List<T> list = (List<T>) entries.get(tClass);
        return list == null ? Collections.emptyList() : list;
    }

    public <T extends IForgeRegistryEntry<T>> void addEntry(Class<T> tClass, T t) {
        getEntries(tClass).add(t);
        if (t instanceof IAlternativeEntries)
            ((IAlternativeEntries) t).addAlternatives(this);
    }

    public <T extends IForgeRegistryEntry<T>> void addEntries(Class<T> tClass, T... ts) {
        for (T t : ts)
            addEntry(tClass, t);
    }

    public final <T extends IForgeRegistryEntry<T>> List<?> getFilteredEntries(Class<T> tClass) {
        return entries.getOrDefault(tClass, new ArrayList<IForgeRegistryEntry<T>>()).stream().filter(o -> !disabledEntries.contains(((IForgeRegistryEntry<T>) (o)).getRegistryName())).collect(Collectors.toList());
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

    public abstract void addEntries();

    public ProtonData getData() {
        return data;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addDisabledEntry(ResourceLocation location) {
        this.disabledEntries.add(location);
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }
}