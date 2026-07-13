/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.loot;

import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TitaniumLootTableProvider extends LootTableProvider {
    private final Supplier<List<Block>> blocksToProcess;

    public TitaniumLootTableProvider(DataGenerator dataGenerator, Supplier<List<Block>> blocks, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
        super(dataGenerator.getPackOutput(), new HashSet<>(), new ArrayList<>(), providerCompletableFuture);
        this.blocksToProcess = blocks;
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return Collections.singletonList(
            new SubProviderEntry(this::createBlockLootTables, LootContextParamSets.BLOCK)
        );
    }

    protected BasicBlockLootTables createBlockLootTables(HolderLookup.Provider prov) {
        return new BasicBlockLootTables(blocksToProcess, prov);
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContextSource validationcontext, ProblemReporter.Collector problemreporter$collector) {
    }
}
