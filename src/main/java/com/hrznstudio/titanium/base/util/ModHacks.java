/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ModHacks {
    public static class ModEventHandlerHack {
        private static final List<Method> METHODS = getMethods();
        private static final Field FIELD = getField();

        static void doHack(TitaniumMod instance) {
            Loader.instance().getModList().stream()
                    .filter(modContainer -> modContainer instanceof FMLModContainer)
                    .filter(modContainer -> modContainer.getModId().equals(instance.getModId()))
                    .forEach(modContainer -> addModEventHandlerMethods((FMLModContainer) modContainer));
        }

        @SuppressWarnings("unchecked")
        private static void addModEventHandlerMethods(FMLModContainer modContainer) {
            ListMultimap<Class<? extends FMLEvent>, Method> methodMap = getModEventMap(modContainer);
            for (Method method : METHODS) {
                Class<? extends FMLEvent> paramClass = (Class<? extends FMLEvent>) method.getParameterTypes()[0];
                methodMap.put(paramClass, method);
            }
        }

        @SuppressWarnings("unchecked")
        private static ListMultimap<Class<? extends FMLEvent>, Method> getModEventMap(FMLModContainer modContainer) {
            try {
                FIELD.setAccessible(true);
                return (ListMultimap<Class<? extends FMLEvent>, Method>) FIELD.get(modContainer);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static List<Method> getMethods() {
            List<Method> foundMethods = new ArrayList<>();
            for (Method method : TitaniumMod.class.getDeclaredMethods()) {
                for (Annotation a : method.getAnnotations()) {
                    if (a.annotationType().equals(Mod.EventHandler.class)) {
                        if (method.getParameterTypes().length == 1 && FMLEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                            method.setAccessible(true);
                            foundMethods.add(method);
                        }
                    }
                }
            }
            return ImmutableList.copyOf(foundMethods);
        }

        private static Field getField() {
            try {
                return FMLModContainer.class.getDeclaredField("eventMethods");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}