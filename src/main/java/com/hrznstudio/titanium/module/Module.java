/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Module {
    private final String id;
    private final Map<String, Feature> featureMap;
    private final Map<String, Feature> disabledFeatureMap = new HashMap<>();
    private final boolean forced;
    private final boolean enabledByDefault;
    private final String description;
    private boolean customFile;

    private Module(String id, Map<String, Feature> featureMap, boolean forced, boolean enabledByDefault, String description, boolean customFile) {
        this.id = id;
        this.featureMap = featureMap;
        this.forced = forced;
        this.enabledByDefault = enabledByDefault;
        this.description = description;
        this.featureMap.values().forEach(f -> f.setModule(this));
        this.customFile = customFile;
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public <T extends IForgeRegistryEntry<T>> List<T> getEntries(Class<T> tClass) {
        List<T> l = new ArrayList<>();
        featureMap.values().forEach(feature -> l.addAll(feature.getEntries(tClass)));
        return l;
    }

    public boolean isForced() {
        return forced;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public String getId() {
        return id;
    }

    public boolean useCustomeFile() {
        return customFile;
    }

    void initConfig(CommentedFileConfig config) {
        new HashMap<>(featureMap).forEach((id, f) -> {
            String cm = "modules." + this.id + "." + id;
            if (!f.isForced()) {
                String c = cm + ".enabled";
                config.setComment(cm, f.getDescription());
                boolean active = config.add(c, f.isEnabledByDefault()) ? f.isEnabledByDefault() : config.getOrElse(c, f.isEnabledByDefault());
                if (active) {
                    f.initConfig(cm, config);
                    f.initEvents();
                } else {
                    disabledFeatureMap.put(id, featureMap.remove(id));
                }
            } else {
                f.initConfig(cm, config);
                f.initEvents();
            }
        });
    }

    public final static class Builder {
        private final String id;
        private final Map<String, Feature> featureMap = new HashMap<>();
        private boolean forced;
        private boolean enabledByDefault = true;
        private String description;
        private boolean customFile = false;

        private Builder(String id) {
            this.id = id;
        }

        private Builder feature(Feature feature) {
            featureMap.put(feature.getId(), feature);
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

        public Builder feature(Feature.Builder feature) {
            return feature(feature.build());
        }

        public Builder useCustomFile() {
            this.customFile = true;
            return this;
        }

        Module build() {
            return new Module(id, featureMap, forced, enabledByDefault, description, customFile);
        }
    }

}