package com.hrznstudio.titanium.plugin;

import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginManager {

    private final String modid;
    private final FeaturePlugin.FeaturePluginType type;
    private final Predicate<FeaturePlugin> predicate;
    private final List<Class> plugins;
    private List<FeaturePluginInstance> instances;

    public PluginManager(String modid, FeaturePlugin.FeaturePluginType type, Predicate<FeaturePlugin> predicate) {
        this.modid = modid;
        this.type = type;
        this.predicate = predicate;
        this.plugins = collect();
    }

    private List<Class> collect() {
        return AnnotationUtil.getFilteredAnnotatedClasses(FeaturePlugin.class, modid).stream().filter(aClass -> aClass.isAssignableFrom(FeaturePluginInstance.class) && ((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class)).type().equals(type) && predicate.test((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class))).collect(Collectors.toList());
    }

    public List<FeaturePluginInstance> getPluginsConstructed() {
        if (instances == null) {
            this.instances = new ArrayList<>();
            this.plugins.forEach(aClass -> {
                try {
                    instances.add((FeaturePluginInstance) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
        return instances;
    }
}
