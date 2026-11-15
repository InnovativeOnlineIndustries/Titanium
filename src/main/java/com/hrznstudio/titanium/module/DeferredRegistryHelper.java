/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistryHelper {

    private final String modId;
    private final HashMap<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries;
    private final IEventBus bus;

    public DeferredRegistryHelper(String modId) {
        this.modId = modId;
        this.registries = new HashMap<>();
        bus = ModList.get().getModContainerById(modId).orElseThrow().getEventBus();
    }

    public <T> DeferredRegister<T> addRegistry(ResourceKey<? extends Registry<T>> key) {
        DeferredRegister<T> deferredRegister = DeferredRegister.create(key, this.modId);
        deferredRegister.register(bus);
        registries.put(key, deferredRegister);
        return deferredRegister;
    }

    private  <T> DeferredHolder<T, T> register(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> object) {
        DeferredRegister<T> deferredRegister = getRegistry(key);
        return deferredRegister.register(name, object);
    }

    public <T> DeferredRegister<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        DeferredRegister<T> deferredRegister = (DeferredRegister<T>)registries.get(key);
        if (deferredRegister == null) {
            this.addRegistry(key);
            deferredRegister = (DeferredRegister<T>)registries.get(key);
        }
        return deferredRegister;
    }

    public <T> DeferredHolder<T, T> registerGeneric(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> object) {
        return this.register(key, name, object);
    }

    public <T, R extends T> DeferredHolder<T, R> registerTyped(ResourceKey<? extends Registry<T>> key, String name, Supplier<R> object) {
        return (DeferredHolder)this.register((ResourceKey)key, name, object);
    }

    public DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> registerBlockEntityType(String name, Supplier<BlockEntityType<?>> object) {
        ResourceKey<Registry<BlockEntityType<?>>> key = Registries.BLOCK_ENTITY_TYPE;
        DeferredRegister<BlockEntityType<?>> deferredRegister = (DeferredRegister<BlockEntityType<?>>) (Object) registries.get(key);
        if (deferredRegister == null) {
            this.addRegistry(key);
            deferredRegister = (DeferredRegister<BlockEntityType<?>>) (Object) registries.get(key);
        }
        return deferredRegister.register(name, object);
    }

    public Holder<EntityType<?>> registerEntityType(String name, Supplier<EntityType<?>> object) {
        ResourceKey<Registry<EntityType<?>>> key = Registries.ENTITY_TYPE;
        DeferredRegister<EntityType<?>> deferredRegister = (DeferredRegister<EntityType<?>>) (Object) registries.get(key);
        if (deferredRegister == null) {
            this.addRegistry(key);
            deferredRegister = (DeferredRegister<EntityType<?>>) (Object) registries.get(key);
        }
        return deferredRegister.register(name, object);
    }

    public DeferredHolder<Block, Block> registerBlockWithItem(String name, Supplier<? extends BasicBlock> blockSupplier, @Nullable TitaniumTab tab) {
        var blockRegistryObject = registerGeneric(Registries.BLOCK, name, blockSupplier::get);
        registerGeneric(Registries.ITEM, name, () -> {
            var item = new BlockItem(blockRegistryObject.get(), new Item.Properties());
            if (tab != null) tab.getTabList().add(item);
            return item;
        });
        return blockRegistryObject;
    }

    public DeferredHolder<Block, Block> registerBlockWithItem(String name, Supplier<? extends Block> blockSupplier, Function<DeferredHolder<Block, Block>, Supplier<Item>> itemSupplier, TitaniumTab tab){
        var block = registerGeneric(Registries.BLOCK, name, blockSupplier::get);
        registerGeneric(Registries.ITEM, name, () -> {
            var item = itemSupplier.apply(block).get();
            if (tab != null) tab.getTabList().add(item);
            return item;
        });
        return block;
    }

    public void registerCapabilities(Holder<BlockEntityType<?>> type) {
        bus.addListener((final RegisterCapabilitiesEvent event) -> {
            event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK, type.value(), (object, context) -> {
                    if (object instanceof PoweredTile<?> powered) {
                        return powered.getEnergyStorage();
                    }
                    return null;
                }
            );
            event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK, type.value(), (object, context) -> {
                    if (object instanceof ActiveTile<?> tile) {
                        return tile.getFluidHandler(context);
                    }
                    return null;
                }
            );
            event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK, type.value(), (object, context) -> {
                    if (object instanceof ActiveTile<?> tile) {
                        return tile.getItemHandler(context);
                    }
                    return null;
                }
            );
        });
    }

    public BlockWithTile registerBlockWithTile(String name, Supplier<BasicTileBlock<?>> blockSupplier, @Nullable TitaniumTab tab){
        DeferredHolder<Block, Block> blockRegistryObject = registerBlockWithItem(name, blockSupplier, tab);
        var type = registerBlockEntityType(name, () -> BlockEntityType.Builder.of(((BasicTileBlock<?>)blockRegistryObject.get()).getTileEntityFactory(), blockRegistryObject.get()).build(null));
        registerCapabilities(type);
        return new BlockWithTile(blockRegistryObject, type);
    }

    public BlockWithTile registerBlockWithTileItem(String name, Supplier<BasicTileBlock<?>> blockSupplier, Function<DeferredHolder<Block, Block>, Supplier<Item>> itemSupplier, @Nullable TitaniumTab tab){
        DeferredHolder<Block, Block> blockRegistryObject = registerBlockWithItem(name, blockSupplier, itemSupplier, tab);
        var type = registerBlockEntityType(name, () -> BlockEntityType.Builder.of(((BasicTileBlock<?>)blockRegistryObject.get()).getTileEntityFactory(), blockRegistryObject.get()).build(null));
        registerCapabilities(type);
        return new BlockWithTile(blockRegistryObject, type);
    }
}
