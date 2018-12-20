/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.util;

import com.google.common.collect.ImmutableList;
//import com.hrznstudio.titanium.api.internal.IColorProvider;
//import com.hrznstudio.titanium.api.internal.IColorProviderItem;
import com.hrznstudio.titanium.api.internal.IItemBlockFactory;
import com.hrznstudio.titanium.api.internal.IModelRegistrar;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public abstract class TitaniumMod {
    private final List<Item> ITEMS = new ArrayList<>();
    private final List<Block> BLOCKS = new ArrayList<>();

    public TitaniumMod() {
//        ModHacks.ModEventHandlerHack.doHack(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public abstract String getModId();

    public final void initTitanium(FMLInitializationEvent event) {
//        SidedHandler.runOn(Dist.CLIENT, () -> () -> { TODO
//            ITEMS.stream()
//                    .filter(IColorProviderItem.class::isInstance)
//                    .map(i -> (Item & IColorProviderItem) i)
//                    .forEach(i -> Minecraft.getInstance().getItemColors().register(i.getColor(), i));
//            BLOCKS.stream()
//                    .filter(IColorProvider.class::isInstance)
//                    .map(b -> (Block & IColorProvider) b)
//                    .forEach(b -> {
//                        Minecraft.getInstance().getItemColors().register(b.getItemColor(), Item.getItemFromBlock(b));
//                        Minecraft.getInstance().getBlockColors().register(b.getColor(), b);
//                    });
//        });
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
        ForgeRegistries.ITEMS.register(item); //TODO unhack
    }

    public void addItems(Item... items) {
        for (Item item : items)
            addItem(item);
    }

    public void addBlock(Block block) {
        BLOCKS.add(block);
        ForgeRegistries.BLOCKS.register(block); //TODO unhack
        if (block instanceof BlockTileBase){
            //((BlockTileBase) block).registerTile();
        }
        if (block instanceof IItemBlockFactory) {
            addItem(((IItemBlockFactory) block).getItemBlockFactory().create());
        }
    }

    public void addBlocks(Block... blocks) {
        for (Block block : blocks)
            addBlock(block);
    }
}