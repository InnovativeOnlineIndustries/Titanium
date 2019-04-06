package com.hrznstudio.titanium.config;

import com.hrznstudio.titanium.annotation.config.ConfigVal;
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
            System.out.println("Scanning class for config : " + configClass.getName());
            builder.push(configClass.getSimpleName());
            try {
                for (Field field : configClass.getFields()) {
                    System.out.println("Found field " + field.getName());
                    if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(ConfigVal.class)) {
                        ConfigVal value = field.getAnnotation(ConfigVal.class);
                        if (!value.comment().isEmpty()) builder.comment(value.comment());
                        cachedConfigValues.put(field, builder.define(value.value().isEmpty() ? field.getName() : value.value(), field.get(null)));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            builder.pop();
        }
        // REGISTERING CONFIG
        ModLoadingContext.get().registerConfig(type.type, builder.build());
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

    public static class Type {
        private ModConfig.Type type;
        private Class[] configClass;

        public Type(ModConfig.Type type, Class... configClass) {
            this.type = type;
            this.configClass = configClass;
        }
    }
}
