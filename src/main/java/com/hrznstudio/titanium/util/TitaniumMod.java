/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class TitaniumMod {
    private final List<Item> ITEMS = new ArrayList<>();
    private final String modid;
    private final List<Block> BLOCKS = new ArrayList<>();

    public TitaniumMod() {
        getMethods().forEach(method -> {
            EventReceiver eventReceiver = method.getAnnotation(EventReceiver.class);
            EventPriority priority = eventReceiver.priority();
            FMLJavaModLoadingContext.get().getModEventBus().addListener(priority, event -> {
                try {
                    if (event.getClass().isAssignableFrom(method.getParameterTypes()[0]))
                        method.invoke(TitaniumMod.this, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
        modid = ModLoadingContext.get().getActiveContainer().getModId();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public List<Method> getMethods() {
        ImmutableList.Builder<Method> builder = new ImmutableList.Builder<>();
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventReceiver.class)) {
                if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    builder.add(method);
                }
            }
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

    @SubscribeEvent
    public final void registerBlocksTitanium(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public final void registerItemsTitanium(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
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
        if (block instanceof BlockTileBase) {
            ((BlockTileBase) block).registerTile();
        }
        if (block instanceof IItemBlockFactory) {
            addItem(((IItemBlockFactory) block).getItemBlockFactory().create());
        }
    }

    public void addBlocks(Block... blocks) {
        for (Block block : blocks)
            addBlock(block);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventReceiver {
        EventPriority priority() default EventPriority.NORMAL;
    }
}