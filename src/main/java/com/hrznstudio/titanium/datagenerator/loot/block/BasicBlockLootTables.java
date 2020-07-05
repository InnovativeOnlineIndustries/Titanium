/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Predicate;

public class BasicBlockLootTables extends BlockLootTables {
    private final Predicate<Block> processBlock;

    public BasicBlockLootTables(String modid) {
        this(block -> Optional.ofNullable(block.getRegistryName())
            .filter(registryName -> registryName.getNamespace().equals(modid))
            .isPresent());
    }

    public BasicBlockLootTables(Predicate<Block> processBlock) {
        this.processBlock = processBlock;
    }

    @Override
    public void addTables() {
        ForgeRegistries.BLOCKS.getValues()
            .stream()
            .filter(processBlock)
            .forEach(block -> {
                if (block instanceof IBlockLootTableProvider) {
                    this.registerLootTable(block, ((IBlockLootTableProvider) block).getLootTableBuilder(this));
                }
            });
    }

    public LootTable.Builder droppingSelf(IItemProvider itemProvider) {
        return LootTable.builder()
            .addLootPool(withSurvivesExplosion(itemProvider, LootPool.builder()
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(itemProvider))));
    }
}
