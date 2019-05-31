/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.api.IAlternativeEntries;
import com.hrznstudio.titanium.module.api.RegistryManager;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feature {
    private final String id;
    private final Map<Class<? extends IForgeRegistryEntry>, List<?>> content;
    private final List<EventManager.FilteredEventManager> events;
    private final boolean forced;
    private final boolean enabledByDefault;
    private final String description;
    private Module module;

    private Feature(String id, Map<Class<? extends IForgeRegistryEntry>, List<?>> content, List<EventManager.FilteredEventManager> events, boolean forced, boolean enabledByDefault, String description) {
        this.id = id;
        this.content = content;
        this.events = events;
        this.forced = forced;
        this.enabledByDefault = enabledByDefault;
        this.description = description;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public Module getModule() {
        return module;
    }

    Feature setModule(Module module) {
        this.module = module;
        return this;
    }

    public boolean isForced() {
        return forced;
    }

    public String getId() {
        return id;
    }

    protected void initConfig(String prefix, CommentedFileConfig config) {
        //Currently Unused
    }

    public <T extends IForgeRegistryEntry<T>> List<? extends T> getEntries(Class<T> tClass) {
        return (List<T>) content.computeIfAbsent(tClass, aClass -> new ArrayList<>());
    }

    public final static class Builder implements RegistryManager<Builder> {
        private final String id;
        private final Map<Class<? extends IForgeRegistryEntry>, List<?>> content = new HashMap<>();
        private final List<EventManager.FilteredEventManager> events = new ArrayList<>();
        private boolean forced;
        private boolean enabledByDefault = true;
        private String description;

        private Builder(String id) {
            this.id = id;
        }

        public <T extends IForgeRegistryEntry<T>> Builder content(Class<T> tClass, T t) {
            getList(tClass).add(t);
            if (t instanceof IAlternativeEntries)
                ((IAlternativeEntries) t).addAlternatives(this);
            return this;
        }

        public Builder event(EventManager.FilteredEventManager manager) {
            events.add(manager);
            return this;
        }

        public Builder force() {
            this.forced = true;
            return this;
        }

        public Builder description(String s) {
            this.description = s;
            return this;
        }

        public Builder disableByDefault() {
            this.enabledByDefault = false;
            return this;
        }

        public <T extends IForgeRegistryEntry<T>> List<T> getList(Class<T> tClass) {
            return (List<T>) content.computeIfAbsent(tClass, aClass -> new ArrayList<>());
        }

        public Feature build() {
            return new Feature(id, content, events, forced, enabledByDefault, description);
        }
    }
}