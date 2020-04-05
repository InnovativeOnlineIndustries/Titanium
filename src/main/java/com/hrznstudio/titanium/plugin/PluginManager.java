package com.hrznstudio.titanium.plugin;

import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.util.AnnotationUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PluginManager {

    private final String modid;
    private final FeaturePlugin.FeaturePluginType type;
    private final Predicate<FeaturePlugin> predicate;
    private final List<Class<? extends FeaturePluginInstance>> plugins;

    public PluginManager(String modid, FeaturePlugin.FeaturePluginType type, Predicate<FeaturePlugin> predicate) {
        this.modid = modid;
        this.type = type;
        this.predicate = predicate;
        this.plugins = collect();
    }

    private List<Class<? extends FeaturePluginInstance>> collect() {
        return AnnotationUtil.getFilteredAnnotatedClasses(FeaturePlugin.class, modid).stream().filter(aClass -> aClass.isAssignableFrom(FeaturePluginInstance.class) && predicate.test((FeaturePlugin) aClass.getAnnotation(FeaturePlugin.class))).collect(Collectors.toList());
    }


}
