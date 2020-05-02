/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.plugin;

import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.util.AnnotationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginManager {

    private final Logger LOGGER = LogManager.getLogger("PluginManager");

    private final String modid;
    private final FeaturePlugin.FeaturePluginType type;
    private final Predicate<FeaturePlugin> predicate;
    private final List<Class> plugins;
    private List<FeaturePluginInstance> instances;
    private final boolean useModIdFilter;

    public PluginManager(String modid, FeaturePlugin.FeaturePluginType type, String featureNameCheck, boolean useModIdFilter) {
        this(modid, type, featurePlugin -> featurePlugin.value().equalsIgnoreCase(featureNameCheck), useModIdFilter);
    }

    public PluginManager(String modid, FeaturePlugin.FeaturePluginType type, Predicate<FeaturePlugin> predicate, boolean useModIdFilter) {
        this.modid = modid;
        this.type = type;
        this.predicate = predicate;
        this.plugins = collect();
        this.useModIdFilter = useModIdFilter;
        this.plugins.forEach(aClass -> LOGGER.info("Found FeaturePluginInstance for class " + aClass.getSimpleName() + " for plugin " + ((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class)).value()));
    }

    private List<Class> collect() {
        LOGGER.info("Scanning classes for " + modid);
        return (useModIdFilter ? AnnotationUtil.getFilteredAnnotatedClasses(FeaturePlugin.class, modid) : AnnotationUtil.getAnnotatedClasses(FeaturePlugin.class)).stream().filter(aClass -> FeaturePluginInstance.class.isAssignableFrom(aClass) && ((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class)).type().equals(type) && predicate.test((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class))).collect(Collectors.toList());
    }

    public List<FeaturePluginInstance> getPluginsConstructed() {
        if (instances == null) {
            this.instances = new ArrayList<>();
            this.plugins.forEach(aClass -> {
                try {
                    instances.add((FeaturePluginInstance) aClass.newInstance());
                    LOGGER.info("Constructed class " + aClass.getSimpleName() + " for plugin " + ((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class)).value() + " for mod " + modid);
                } catch (InstantiationException | IllegalAccessException e) {
                    LOGGER.error(e);
                }
            });
        }
        return instances;
    }

    public void execute(PluginPhase phase) {
        getPluginsConstructed().forEach(featurePluginInstance -> {
            LOGGER.info("Executing phase " + phase.toString() + " for plugin class " + featurePluginInstance.getClass().getSimpleName());
            featurePluginInstance.execute(phase);
        });
    }

}
