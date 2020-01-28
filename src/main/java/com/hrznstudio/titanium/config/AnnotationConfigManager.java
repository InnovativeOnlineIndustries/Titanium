/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.config;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;
import com.hrznstudio.titanium.util.AnnotationUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnotationConfigManager {

    public List<Type> configClasses;
    public HashMap<Field, ForgeConfigSpec.ConfigValue> cachedConfigValues;

    public AnnotationConfigManager() {
        configClasses = new ArrayList<>();
        cachedConfigValues = new HashMap<>();
    }

    public void add(Type type) {
        configClasses.add(type);
        // SCANNING CLASSES
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        for (Class configClass : type.configClass) {
            scanClass(configClass, builder);
        }
        // REGISTERING CONFIG
        String fileName = ModLoadingContext.get().getActiveContainer().getModId() + "/" + (type.fileName.isEmpty() ? ModLoadingContext.get().getActiveContainer().getModId() : type.fileName);
        if (!fileName.endsWith(".toml")) {
            fileName = fileName + ".toml";
        }
        ModLoadingContext.get().registerConfig(type.type, builder.build(), fileName);
    }

    private void scanClass(Class configClass, ForgeConfigSpec.Builder builder) {
        builder.push(configClass.getSimpleName());
        try {
            for (Field field : configClass.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigVal.class)) {
                    if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                        ConfigVal value = field.getAnnotation(ConfigVal.class);
                        ForgeConfigSpec.ConfigValue configValue = null;
                        if (!value.comment().isEmpty()) {
                            builder.comment(value.comment());
                        }

                        if (field.isAnnotationPresent(ConfigVal.InRangeDouble.class)) {
                            configValue = builder.defineInRange(value.value().isEmpty() ? field.getName() : value.value(), (Double) field.get(null),
                                    field.getAnnotation(ConfigVal.InRangeDouble.class).min(), field.getAnnotation(ConfigVal.InRangeDouble.class).max());
                        }
                        if (field.isAnnotationPresent(ConfigVal.InRangeLong.class)) {
                            configValue = builder.defineInRange(value.value().isEmpty() ? field.getName() : value.value(), (Long) field.get(null),
                                    field.getAnnotation(ConfigVal.InRangeLong.class).min(), field.getAnnotation(ConfigVal.InRangeLong.class).max());
                        }
                        if (field.isAnnotationPresent(ConfigVal.InRangeInt.class)) {
                            configValue = builder.defineInRange(value.value().isEmpty() ? field.getName() : value.value(), (Integer) field.get(null),
                                    field.getAnnotation(ConfigVal.InRangeInt.class).min(), field.getAnnotation(ConfigVal.InRangeInt.class).max());
                        }

                        if (configValue == null) {
                            configValue = builder.define(value.value().isEmpty() ? field.getName() : value.value(), field.get(null));
                        }
                        cachedConfigValues.put(field, configValue);
                    } else {
                        scanClass(field.getType(), builder);
                    }
                }
            }
            AnnotationUtil.getFilteredAnnotatedClasses(ConfigFile.Child.class, ModLoadingContext.get().getActiveContainer().getModId()).stream()
                    .filter(aClass -> ((ConfigFile.Child) aClass.getAnnotation(ConfigFile.Child.class)).value().equals(configClass)).forEach(aClass -> scanClass(aClass, builder));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        builder.pop();
    }

    public void inject() {
        // MODIFYING CLASSES VALUES FROM CONFIG
        cachedConfigValues.forEach((field, configValue) -> {
            try {
                field.set(null, configValue.get());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isClassManaged(Class clazz) {
        for (Type configClass : configClasses) {
            for (Class aClass : configClass.configClass) {
                if (clazz.equals(aClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class Type {
        private ModConfig.Type type;
        private Class[] configClass;
        private String fileName;

        private Type(ModConfig.Type type, Class... configClass) {
            this.type = type;
            this.configClass = configClass;
            this.fileName = "";
        }

        public static Type client(Class... classes) {
            return new Type(ModConfig.Type.CLIENT, classes);
        }

        public static Type common(Class... classes) {
            return new Type(ModConfig.Type.COMMON, classes);
        }

        public static Type server(Class... classes) {
            return new Type(ModConfig.Type.SERVER, classes);
        }

        public static Type of(ModConfig.Type type, Class... classes) {
            return new Type(type, classes);
        }

        public Type setName(String name) {
            this.fileName = name;
            return this;
        }

        public Class[] getConfigClass() {
            return configClass;
        }
    }
}