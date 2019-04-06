/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class TitaniumMod {
    private final List<Item> ITEMS = new ArrayList<>();
    private final String modid;
    private final List<Block> BLOCKS = new ArrayList<>();
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

    @EventReceiver
    public final void clientSetupTitanium(FMLClientSetupEvent event) {
        ITEMS.stream()
                .filter(IItemColorProvider.class::isInstance)
                .map(i -> (Item & IItemColorProvider) i)
                .forEach(i -> Minecraft.getInstance().getItemColors().register(((IItemColorProvider) i)::getColor, i));
        BLOCKS.stream()
                .filter(IColorProvider.class::isInstance)
                .map(b -> (Block & IColorProvider) b)
                .forEach(b -> {
                    Minecraft.getInstance().getItemColors().register(((IColorProvider) b)::getColor, b.asItem());
                    Minecraft.getInstance().getBlockColors().register(((IColorProvider) b)::getColor, b);
                });
    }

    @EventReceiver
    public final void registerBlocksTitanium(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(event.getRegistry()::register);
    }

    @EventReceiver
    public final void registerTilesTitanium(RegistryEvent.Register<TileEntityType<?>> event) {
        BLOCKS.stream().filter(BlockTileBase.class::isInstance).map(BlockTileBase.class::cast).forEach(block -> {
            block.registerTile(event.getRegistry());
        });
    }

    @EventReceiver
    public final void registerItemsTitanium(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(event.getRegistry()::register);
    }

    @EventReceiver
    public final void modelRegistryEventTitanium(ModelRegistryEvent event) {
        if (!BLOCKS.isEmpty())
            BLOCKS.stream()
                    .filter(IModelRegistrar.class::isInstance)
                    .map(IModelRegistrar.class::cast)
                    .forEach(IModelRegistrar::registerModels);
        if (!ITEMS.isEmpty())
            ITEMS.stream()
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

    public List<Block> getBlocks() {
        return ImmutableList.copyOf(BLOCKS);
    }

    public List<Item> getItems() {
        return ImmutableList.copyOf(ITEMS);
    }

    public void addItem(Item item) {
        ITEMS.add(item);
    }

    public void addItems(Item... items) {
        for (Item item : items)
            addItem(item);
    }

    public void addBlock(Block block) {
        BLOCKS.add(block);
        if (block instanceof IItemBlockFactory) {
            addItem(((IItemBlockFactory) block).getItemBlockFactory().create());
        }
    }

    public void addBlocks(Block... blocks) {
        for (Block block : blocks)
            addBlock(block);
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