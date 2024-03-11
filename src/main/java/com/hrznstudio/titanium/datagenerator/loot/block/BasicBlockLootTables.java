/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.loot.block;

import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.util.NonNullLazy;

import java.util.List;

public class BasicBlockLootTables extends BlockLootTables {
    private final NonNullLazy<List<Block>> blocksToProcess;

    public BasicBlockLootTables(NonNullLazy<List<Block>> blocksToProcess) {
        this.blocksToProcess = blocksToProcess;
    }

    @Override
    public void addTables() {
        blocksToProcess.get()
            .forEach(block -> {
                if (block instanceof IBlockLootTableProvider) {
                    this.registerLootTable(block, ((IBlockLootTableProvider) block).getLootTable(this));
                }
            });
    }

    public LootTable.Builder droppingNothing() {
        return LootTable.builder();
    }

    public LootTable.Builder droppingSelf(IItemProvider itemProvider) {
        return LootTable.builder()
            .addLootPool(withSurvivesExplosion(itemProvider, LootPool.builder()
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(itemProvider))));
    }

    public LootTable.Builder droppingSelfWithNbt(IItemProvider itemProvider, CopyNbt.Builder nbtBuilder) {
        return LootTable.builder()
            .addLootPool(withSurvivesExplosion(itemProvider, LootPool.builder()
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(itemProvider).acceptFunction(nbtBuilder))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return blocksToProcess.get();
    }
}
