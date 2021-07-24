/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.plugin.PluginManager;
import com.hrznstudio.titanium.plugin.PluginPhase;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ModuleController {
    private final String modid;
    private final Map<String, Module> moduleMap = new HashMap<>();
    private final Map<String, Module> disabledModuleMap = new HashMap<>();
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();
    private final PluginManager modPluginManager;

    public ModuleController() {
        this.modid = ModLoadingContext.get().getActiveContainer().getModId();
        this.modPluginManager = new PluginManager(modid, FeaturePlugin.FeaturePluginType.MOD, featurePlugin -> ModList.get().isLoaded(featurePlugin.value()), true);
        this.modPluginManager.execute(PluginPhase.CONSTRUCTION);
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
        if (!moduleMap.isEmpty()) {
            File modulesConfig = new File("config/" + modid + "/modules.toml");
            if (!modulesConfig.exists()) {
                modulesConfig.getParentFile().mkdirs();
            }
            CommentedFileConfig config = CommentedFileConfig.of(modulesConfig);
            config.load();
            new HashMap<>(moduleMap).forEach((id, m) -> {
                CommentedFileConfig tempFile = config;
                if (m.useCustomeFile()) {
                    File temp = new File("config/" + modid + "/" + id + ".toml");
                    if (!temp.exists()) {
                        temp.getParentFile().mkdirs();
                    }
                    tempFile = CommentedFileConfig.of(temp);
                    tempFile.load();
                }
                if (!m.isForced()) {
                    String cm = "modules." + id;
                    String c = cm + ".enabled";
                    tempFile.setComment(cm, m.getDescription());
                    boolean active = tempFile.add(c, m.isEnabledByDefault()) ? m.isEnabledByDefault() : config.getOrElse(c, m.isEnabledByDefault());
                    if (active) {
                        m.initConfig(tempFile);
                    } else {
                        disabledModuleMap.put(id, moduleMap.remove(id));
                    }
                } else {
                    m.initConfig(tempFile);
                }
                if (m.useCustomeFile()) {
                    tempFile.save();
                    tempFile.close();
                }
            });
            config.save();
            config.close();
        }
        moduleMap.values().stream().map(module -> module.getFeatureMap().values()).flatMap(Collection::stream).map(feature -> feature.getContent().keySet()).flatMap(Collection::stream).distinct().forEach(aClass -> {
            EventManager.modGeneric(RegistryEvent.Register.class, aClass).process(register -> {
                moduleMap.values().forEach(m -> {
                    List<? extends IForgeRegistryEntry<?>> l = m.getEntries((Class) aClass);
                    if (!l.isEmpty())
                        l.forEach(((RegistryEvent.Register) register).getRegistry()::register);
                });
            }).subscribe();
        });
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

    public void addModule(Module module) {
        moduleMap.put(module.getId(), module);
    }

    public void addModule(Module.Builder builder) {
        addModule(builder.build());
    }

    public void addDataProvider(GatherDataEvent event) {

    }
}
