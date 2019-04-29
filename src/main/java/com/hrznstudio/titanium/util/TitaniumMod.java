/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.util;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.api.internal.IColorProvider;
import com.hrznstudio.titanium.api.internal.IItemBlockFactory;
import com.hrznstudio.titanium.api.internal.IItemColorProvider;
import com.hrznstudio.titanium.api.internal.IModelRegistrar;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.config.AnnotationConfigManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class TitaniumMod {
    private final String modid;
    private final Map<Class<? extends IForgeRegistryEntry>, List<?>> ENTRIES = new HashMap<>();
    private final AnnotationConfigManager configManager = new AnnotationConfigManager();

    public TitaniumMod() {
        MinecraftForge.EVENT_BUS.register(this);
        List<Method> methods = getMethods();
        methods.forEach(method -> {
            EventReceiver eventReceiver = method.getAnnotation(EventReceiver.class);
            EventPriority priority = eventReceiver.priority();
            FMLJavaModLoadingContext.get().getModEventBus().addListener(priority, event -> {
                try {
                    Class<?> param = method.getParameterTypes()[0];
                    if (event.getClass().isAssignableFrom(param)) {
                        if (GenericEvent.class.isAssignableFrom(param)) {
                            if (event instanceof GenericEvent) {
                                Type[] arr = method.getGenericParameterTypes();
                                Type type;
                                ParameterizedType parameterizedType = (ParameterizedType) arr[0];
                                Type actual = parameterizedType.getActualTypeArguments()[0];
                                if (actual instanceof ParameterizedType) {
                                    type = ((ParameterizedType) actual).getRawType();
                                } else {
                                    type = actual;
                                }
                                if (type == ((GenericEvent) event).getGenericType()) {
                                    method.invoke(this, event);
                                }
                            }
                        } else {
                            method.invoke(this, event);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> {
            if (event instanceof RegistryEvent.Register) {
                getEntries((Class<IForgeRegistryEntry>) ((RegistryEvent) event).getGenericType()).forEach(t -> ((RegistryEvent.Register) event).getRegistry().register((IForgeRegistryEntry) t));
                if (((RegistryEvent.Register) event).getGenericType() == TileEntityType.class) {
                    getEntries(Block.class).stream().filter(BlockTileBase.class::isInstance).map(BlockTileBase.class::cast).forEach(block -> block.registerTile(((RegistryEvent.Register) event).getRegistry()));
                }
            }
        });
        modid = ModLoadingContext.get().getActiveContainer().getModId();
    }

    public List<Method> getMethods() {
        ImmutableList.Builder<Method> builder = new ImmutableList.Builder<>();
        Class clazz = getClass();
        while (clazz != null) {
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventReceiver.class)) {
                    if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        builder.add(method);
                    }
                }
            }
            clazz = null;
        }
        return builder.build();
    }

    public <T extends IForgeRegistryEntry<T>> List<T> getEntries(Class<T> tClass) {
        if (!ENTRIES.containsKey(tClass)) {
            ENTRIES.put(tClass, new ArrayList<T>());
        }
        List<T> list = (List<T>) ENTRIES.get(tClass);
        return list == null ? Collections.emptyList() : list;
    }

    @EventReceiver
    public final void clientSetupTitanium(FMLClientSetupEvent event) {
        getEntries(Item.class).stream()
                .filter(IItemColorProvider.class::isInstance)
                .map(i -> (Item & IItemColorProvider) i)
                .forEach(i -> Minecraft.getInstance().getItemColors().register(((IItemColorProvider) i)::getColor, i));
        getEntries(Block.class).stream()
                .filter(IColorProvider.class::isInstance)
                .map(b -> (Block & IColorProvider) b)
                .forEach(b -> {
                    Minecraft.getInstance().getItemColors().register(((IColorProvider) b)::getColor, b.asItem());
                    Minecraft.getInstance().getBlockColors().register(((IColorProvider) b)::getColor, b);
                });
    }


    @EventReceiver
    public final void modelRegistryEventTitanium(ModelRegistryEvent event) {
        List<Block> blocks = getEntries(Block.class);
        if (!blocks.isEmpty())
            blocks.stream()
                    .filter(IModelRegistrar.class::isInstance)
                    .map(IModelRegistrar.class::cast)
                    .forEach(IModelRegistrar::registerModels);
        List<Item> items = getEntries(Item.class);
        if (!items.isEmpty())
            items.stream()
                    .filter(IModelRegistrar.class::isInstance)
                    .map(IModelRegistrar.class::cast)
                    .forEach(IModelRegistrar::registerModels);
    }

    @EventReceiver
    public final void onConfig(ModConfig.Loading event) {
        configManager.inject();
    }

    @EventReceiver
    public final void onConfigReload(ModConfig.ConfigReloading event) {
        configManager.inject();
    }

    public <T extends IForgeRegistryEntry<T>> void addEntry(Class<T> tClass, T t) {
        getEntries(tClass).add(t);
        if (t instanceof IItemBlockFactory)
            addEntry(Item.class, ((IItemBlockFactory) t).getItemBlockFactory().create());
    }

    public <T extends IForgeRegistryEntry<T>> void addEntries(Class<T> tClass, T... ts) {
        for (T t : ts)
            addEntry(tClass, t);
    }

    public void addConfig(AnnotationConfigManager.Type type) {
        configManager.add(type);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventReceiver {
        EventPriority priority() default EventPriority.NORMAL;
    }
}