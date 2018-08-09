/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.util;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.base.api.internal.IColorProvider;
import com.hrznstudio.titanium.base.api.internal.IColorProviderItem;
import com.hrznstudio.titanium.base.api.internal.IItemBlockFactory;
import com.hrznstudio.titanium.base.api.internal.IModelRegistrar;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public abstract class TitaniumMod {
    private static final List<Item> ITEMS = new ArrayList<>();
    private static final List<Block> BLOCKS = new ArrayList<>();

    public TitaniumMod() {
        ModHacks.ModEventHandlerHack.doHack(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public abstract String getModId();

    @Mod.EventHandler
    public final void initTitanium(FMLInitializationEvent event) {
        SidedHandler.runOn(Side.CLIENT, () -> () -> {
            ITEMS.stream()
                    .filter(IColorProviderItem.class::isInstance)
                    .map(i -> (Item & IColorProviderItem) i)
                    .forEach(i -> Minecraft.getMinecraft().getItemColors().registerItemColorHandler(i.getColor(), i));
            BLOCKS.stream()
                    .filter(IColorProvider.class::isInstance)
                    .map(b -> (Block & IColorProvider) b)
                    .forEach(b -> {
                        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(b.getItemColor(), Item.getItemFromBlock(b));
                        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(b.getColor(), b);
                    });
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
    @SideOnly(Side.CLIENT)
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
        if (block instanceof IItemBlockFactory) {
            addItem(((IItemBlockFactory) block).getItemBlockFactory().create());
        }
    }

    public void addBlocks(Block... blocks) {
        for (Block block : blocks)
            addBlock(block);
    }
}