/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


public class BlockRegistryObjectGroup<B extends Block, I extends Item, T extends BlockEntity> implements Supplier<B> {
    private final String name;
    private final Supplier<B> blockCreator;
    private final Function<B, I> itemCreator;
    private final BlockEntityType.BlockEntitySupplier<T> tileSupplier;

    private RegistryObject<B> block;
    private RegistryObject<I> item;
    private RegistryObject<BlockEntityType<T>> tileEntity;

    public BlockRegistryObjectGroup(String name, Supplier<B> blockCreator, Function<B, I> itemCreator) {
        this(name, blockCreator, itemCreator, null);
    }

    public BlockRegistryObjectGroup(String name, Supplier<B> blockCreator, Function<B, I> itemCreator, Supplier<T> tileSupplier) {
        this.name = name;
        this.blockCreator = blockCreator;
        this.itemCreator = itemCreator;
        this.tileSupplier = new BlockEntityType.BlockEntitySupplier<T>() {
            @Override
            public T create(BlockPos p_155268_, BlockState p_155269_) {
                return tileSupplier.get();
            }
        };
    }

    @Nonnull
    public B getBlock() {
        return Objects.requireNonNull(block).get();
    }

    @Nonnull
    public I getItem() {
        return Objects.requireNonNull(item).get();
    }

    @Nonnull
    public BlockEntityType<T> getTileEntityType() {
        return Objects.requireNonNull(tileEntity).get();
    }

    public BlockRegistryObjectGroup<B, I, ?> register(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
        block = blockRegistry.register(name, blockCreator);
        item = itemRegistry.register(name, () -> itemCreator.apply(this.getBlock()));
        return this;
    }

    public BlockRegistryObjectGroup<B, I, T> register(DeferredRegister<Block> blockRegistry,
                                                      DeferredRegister<Item> itemRegistry,
                                                      DeferredRegister<BlockEntityType<?>> tileEntityTypeRegistry) {
        this.register(blockRegistry, itemRegistry);
        if (tileSupplier != null) {
            //noinspection ConstantConditions
            tileEntity = tileEntityTypeRegistry.register(name, () -> BlockEntityType.Builder.of(tileSupplier, this.getBlock())
                    .build(null));
        }
        return this;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public B get() {
        return this.getBlock();
    }
}
