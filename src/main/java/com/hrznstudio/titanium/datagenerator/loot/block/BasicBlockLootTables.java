/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.loot.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class BasicBlockLootTables extends BlockLootSubProvider {
    private final Supplier<List<Block>> blocksToProcess;

    public BasicBlockLootTables(Supplier<List<Block>> blocksToProcess, HolderLookup.Provider provider) {
        super(new HashSet<>(), FeatureFlags.REGISTRY.allFlags(), provider);
        this.blocksToProcess = blocksToProcess;
    }


    public LootTable.Builder droppingNothing() {
        return LootTable.lootTable();
    }

    public LootTable.Builder droppingSelf(ItemLike itemProvider) {
        return LootTable.lootTable()
            .withPool(applyExplosionCondition(itemProvider, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(itemProvider))));
    }

    @Override
    protected void generate() {
        blocksToProcess.get()
            .forEach(block -> {
                if (block instanceof IBlockLootTableProvider) {
                    this.add(block, ((IBlockLootTableProvider) block).getLootTable(this));
                }
            });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return blocksToProcess.get();
    }
}
