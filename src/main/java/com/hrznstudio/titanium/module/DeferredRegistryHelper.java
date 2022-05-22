/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.block.BasicTileBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegistryHelper {

    private final String modId;
    private HashMap<Class<? extends IForgeRegistryEntry>, DeferredRegister<? extends IForgeRegistryEntry>> registries;

    public DeferredRegistryHelper(String modId) {
        this.modId = modId;
        this.registries = new HashMap<>();
    }

    public DeferredRegister<? extends IForgeRegistryEntry> addRegistry(Class<? extends IForgeRegistryEntry> cl) {
        DeferredRegister deferredRegister = DeferredRegister.create(cl, this.modId);
        deferredRegister.register(FMLJavaModLoadingContext.get().getModEventBus());
        //registries.put(cl, deferredRegister);
        return deferredRegister;
    }

    private  <T extends IForgeRegistryEntry<? super T>> RegistryObject<T> register(Class<T> cl, String name, Supplier<T> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(cl, this::addRegistry);
        /*if (j instanceof IAlternativeEntries) { // I'm sorry
            ((IAlternativeEntries) j).addAlternatives(this);
        }*/
        return deferredRegister.register(name, object);
    }

    public <T extends IForgeRegistryEntry<? super T>> RegistryObject<T> registerGeneric(Class<T> cl, String name, Supplier<T> object) {
        return this.register(cl, name, object);
    }

    public RegistryObject<BlockEntityType<?>> registerBlockEntityType(String name, Supplier<?> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(BlockEntityType.class, this::addRegistry);
        return deferredRegister.register(name, object);
    }

    public RegistryObject<EntityType<?>> registerEntityType(String name, Supplier<?> object) {
        DeferredRegister deferredRegister = registries.computeIfAbsent(EntityType.class, this::addRegistry);
        return deferredRegister.register(name, object);
    }

    public RegistryObject<Block> registerBlockWithItem(String name, Supplier<? extends BasicBlock> blockSupplier){
        RegistryObject<Block> blockRegistryObject = registerGeneric(Block.class, name, blockSupplier::get);
        registerGeneric(Item.class, name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().tab(((BasicBlock) blockRegistryObject.get()).getItemGroup())));
        return blockRegistryObject;
    }

    public RegistryObject<Block> registerBlockWithItem(String name, Supplier<? extends Block> blockSupplier, Function<RegistryObject<Block>, Supplier<Item>> itemSupplier){
        DeferredRegister blockDeferredRegister = registries.computeIfAbsent(Block.class, this::addRegistry);
        DeferredRegister itemDeferredRegister = registries.computeIfAbsent(Item.class, this::addRegistry);
        RegistryObject<Block> block = blockDeferredRegister.register(name, blockSupplier);
        itemDeferredRegister.register(name, itemSupplier.apply(block));
        return block;
    }

    public Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> registerBlockWithTile(String name, Supplier<BasicTileBlock> blockSupplier){
        RegistryObject<Block> blockRegistryObject = registerBlockWithItem(name, blockSupplier);
        return Pair.of(blockRegistryObject, registerBlockEntityType(name, () -> BlockEntityType.Builder.of(((BasicTileBlock<?>)blockRegistryObject.get()).getTileEntityFactory(), blockRegistryObject.get()).build(null)));
    }
}
