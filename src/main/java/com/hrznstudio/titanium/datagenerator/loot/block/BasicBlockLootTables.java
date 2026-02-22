/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.loot.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
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
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> p_249322_) {
        this.generate();
        Set<ResourceKey<LootTable>> set = new HashSet<>();

        for(Block block : getKnownBlocks()) {
            if (block.isEnabled(this.enabledFeatures)) {
                var resourcelocation = block.getLootTable();
                if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                    LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourcelocation, BuiltInRegistries.BLOCK.getKey(block)));
                    }

                    p_249322_.accept(resourcelocation, loottable$builder);
                }
            }
        }

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return blocksToProcess.get();
    }
}
