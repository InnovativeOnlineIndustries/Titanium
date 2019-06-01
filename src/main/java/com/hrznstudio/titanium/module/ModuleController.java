/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ModuleController {
    private final String modid;
    private final Map<String, Module> moduleMap = new HashMap<>();
    private final Map<String, Module> disabledModuleMap = new HashMap<>();
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();

    public ModuleController() {
        modid = ModLoadingContext.get().getActiveContainer().getModId();
        initModules();
        File modulesConfig = new File("config/" + modid + "/modules.toml");
        if (!modulesConfig.exists()) {
            modulesConfig.getParentFile().mkdirs();
        }
        CommentedFileConfig config = CommentedFileConfig.of(modulesConfig);
        config.load();
        new HashMap<>(moduleMap).forEach((id, m) -> {
            if (!m.isForced()) {
                String cm = "modules." + id;
                String c = cm + ".enabled";
                config.setComment(cm, m.getDescription());
                boolean active = config.add(c, m.isEnabledByDefault()) ? m.isEnabledByDefault() : config.getOrElse(c, m.isEnabledByDefault());
                if (active) {
                    m.initConfig(config);
                } else {
                    disabledModuleMap.put(id, moduleMap.remove(id));
                }
            }
        });
        config.save();
        config.close();

        EventManager.mod(RegistryEvent.Register.class).process(event -> {
            moduleMap.values().forEach(m -> {
                List<? extends IForgeRegistryEntry<?>> l = m.getEntries((Class) event.getGenericType());
                if (!l.isEmpty())
                    l.forEach(event.getRegistry()::register);
            });
        }).subscribe();
        AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.class, modid).forEach(aClass -> {
            ConfigFile annotation = (ConfigFile) aClass.getAnnotation(ConfigFile.class);
            addConfig(AnnotationConfigManager.Type.of(annotation.type(), aClass).setName(annotation.value()));
        });
        EventManager.mod(ModConfig.Loading.class).process(ev -> configManager.inject()).subscribe();
        EventManager.mod(ModConfig.ConfigReloading.class).process(ev -> configManager.inject()).subscribe();
    }

    private void addConfig(AnnotationConfigManager.Type type) {
        for (Class configClass : type.getConfigClass()) {
            if (configManager.isClassManaged(configClass)) return;
        }
        configManager.add(type);
    }

    protected abstract void initModules();

    public void addModule(Module module) {
        moduleMap.put(module.getId(), module);
    }

    public void addModule(Module.Builder builder) {
        addModule(builder.build());
    }
}