/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistryHelper {

    private final String modId;
    private final HashMap<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> registries;
    private final CreativeTabHelper creativeTabHelper;

    public DeferredRegistryHelper(String modId) {
        this.modId = modId;
        this.registries = new HashMap<>();
        this.creativeTabHelper = new CreativeTabHelper();
    }

    public <T> DeferredRegister<T> addRegistry(ResourceKey<? extends Registry<T>> key) {
        DeferredRegister<T> deferredRegister = DeferredRegister.create(key, this.modId);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        registries.put(key, deferredRegister);
        return deferredRegister;
    }

    private  <T> RegistryObject<T> register(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> object) {
        DeferredRegister<T> deferredRegister = (DeferredRegister<T>)(Object)registries.get(key);
        if (deferredRegister == null) {
            this.addRegistry(key);
            deferredRegister = (DeferredRegister<T>)(Object)registries.get(key);
        }
        return deferredRegister.register(name, object);
    }

    public <T> RegistryObject<T> registerGeneric(ResourceKey<? extends Registry<T>> key, String name, Supplier<T> object) {
        return this.register(key, name, object);
    }

    public RegistryObject<BlockEntityType<?>> registerBlockEntityType(String name, Supplier<BlockEntityType<?>> object) {
        ResourceKey<Registry<BlockEntityType<?>>> key = ForgeRegistries.BLOCK_ENTITY_TYPES.getRegistryKey();
        DeferredRegister<BlockEntityType<?>> deferredRegister = (DeferredRegister<BlockEntityType<?>>) (Object) registries.get(key);
        if (deferredRegister == null) {
            this.addRegistry(key);
            deferredRegister = (DeferredRegister<BlockEntityType<?>>) (Object) registries.get(key);
        }
        return deferredRegister.register(name, object);
    }

    public RegistryObject<EntityType<?>> registerEntityType(String name, Supplier<EntityType<?>> object) {
        ResourceKey<Registry<EntityType<?>>> key = ForgeRegistries.ENTITY_TYPES.getRegistryKey();
        DeferredRegister<EntityType<?>> deferredRegister = (DeferredRegister<EntityType<?>>) (Object) registries.get(key);
        if (deferredRegister == null) {
            this.addRegistry(key);
            deferredRegister = (DeferredRegister<EntityType<?>>) (Object) registries.get(key);
        }
        return deferredRegister.register(name, object);
    }

    public RegistryObject<Block> registerBlockWithItem(String name, Supplier<? extends BasicBlock> blockSupplier, @Nullable TitaniumTab tab) {
        RegistryObject<Block> blockRegistryObject = registerGeneric(ForgeRegistries.BLOCKS.getRegistryKey(), name, blockSupplier::get);
        registerGeneric(ForgeRegistries.ITEMS.getRegistryKey(), name, () -> {
            var item = new BlockItem(blockRegistryObject.get(), new Item.Properties());
            if (tab != null) tab.getTabList().add(item);
            return item;
        });
        return blockRegistryObject;
    }

    public RegistryObject<Block> registerBlockWithItem(String name, Supplier<? extends Block> blockSupplier, Function<RegistryObject<Block>, Supplier<Item>> itemSupplier, TitaniumTab tab){
        RegistryObject<Block> block = registerGeneric(ForgeRegistries.BLOCKS.getRegistryKey(), name, blockSupplier::get);
        registerGeneric(ForgeRegistries.ITEMS.getRegistryKey(), name, () -> {
            var item = itemSupplier.apply(block).get();
            if (tab != null) tab.getTabList().add(item);
            return item;
        });
        return block;
    }

    public Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> registerBlockWithTile(String name, Supplier<BasicTileBlock<?>> blockSupplier, @Nullable TitaniumTab tab){
        RegistryObject<Block> blockRegistryObject = registerBlockWithItem(name, blockSupplier, tab);
        return Pair.of(blockRegistryObject, registerBlockEntityType(name, () -> BlockEntityType.Builder.of(((BasicTileBlock<?>)blockRegistryObject.get()).getTileEntityFactory(), blockRegistryObject.get()).build(null)));
    }

    public Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> registerBlockWithTileItem(String name, Supplier<BasicTileBlock<?>> blockSupplier, Function<RegistryObject<Block>, Supplier<Item>> itemSupplier, @Nullable TitaniumTab tab){
        RegistryObject<Block> blockRegistryObject = registerBlockWithItem(name, blockSupplier, itemSupplier, tab);
        return Pair.of(blockRegistryObject, registerBlockEntityType(name, () -> BlockEntityType.Builder.of(((BasicTileBlock<?>)blockRegistryObject.get()).getTileEntityFactory(), blockRegistryObject.get()).build(null)));
    }
}
