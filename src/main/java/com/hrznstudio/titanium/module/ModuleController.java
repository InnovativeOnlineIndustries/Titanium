/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.plugin.PluginManager;
import com.hrznstudio.titanium.plugin.PluginPhase;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public abstract class ModuleController {
    private final String modid;
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();
    private final PluginManager modPluginManager;
    private final DeferredRegistryHelper deferredRegistryHelper;

    public ModuleController() {
        this.modid = ModLoadingContext.get().getActiveContainer().getModId();
        this.modPluginManager = new PluginManager(modid, FeaturePlugin.FeaturePluginType.MOD, featurePlugin -> ModList.get().isLoaded(featurePlugin.value()), true);
        this.modPluginManager.execute(PluginPhase.CONSTRUCTION);
        this.deferredRegistryHelper = new DeferredRegistryHelper(this.modid);
        onPreInit();
        onInit();
        onPostInit();
    }

    private void addConfig(AnnotationConfigManager.Type type) {
        for (Class configClass : type.getConfigClass()) {
            if (configManager.isClassManaged(configClass)) return;
        }
        configManager.add(type);
    }

    public void onPreInit() {
        this.modPluginManager.execute(PluginPhase.PRE_INIT);
    }

    public void onInit() {
        initModules();

        this.modPluginManager.execute(PluginPhase.INIT);
    }

    public void onPostInit() {
        AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.class, modid).forEach(aClass -> {
            ConfigFile annotation = (ConfigFile) aClass.getAnnotation(ConfigFile.class);
            addConfig(AnnotationConfigManager.Type.of(annotation.type(), aClass).setName(annotation.value()));
        });
        EventManager.mod(ModConfigEvent.Loading.class).process(ev -> {
            configManager.inject();
            this.modPluginManager.execute(PluginPhase.CONFIG_LOAD);
        }).subscribe();
        EventManager.mod(ModConfigEvent.Reloading.class).process(ev -> {
            configManager.inject();
            this.modPluginManager.execute(PluginPhase.CONFIG_RELOAD);
        }).subscribe();
        EventManager.mod(GatherDataEvent.class).process(this::addDataProvider).subscribe();
        EventManager.mod(FMLClientSetupEvent.class).process(fmlClientSetupEvent -> this.modPluginManager.execute(PluginPhase.CLIENT_SETUP)).subscribe();
        EventManager.mod(FMLCommonSetupEvent.class).process(fmlClientSetupEvent -> this.modPluginManager.execute(PluginPhase.COMMON_SETUP)).subscribe();
        this.modPluginManager.execute(PluginPhase.POST_INIT);
    }

    protected abstract void initModules();

    public void addDataProvider(GatherDataEvent event) {

    }

    public DeferredRegistryHelper getRegistries() {
        return deferredRegistryHelper;
    }
}
