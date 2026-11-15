/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.module;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public record BlockWithTile(DeferredHolder<Block, Block> block, DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> type) implements ItemLike {
    @Override
    public Item asItem() {
        return block.get().asItem();
    }

    public Block getBlock() {
        return block.get();
    }

}
