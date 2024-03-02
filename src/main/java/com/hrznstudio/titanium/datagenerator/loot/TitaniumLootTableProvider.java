/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.loot;

import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.util.NonNullLazy;

import java.util.*;

public class TitaniumLootTableProvider extends LootTableProvider {
    private final NonNullLazy<List<Block>> blocksToProcess;

    public TitaniumLootTableProvider(DataGenerator dataGenerator, NonNullLazy<List<Block>> blocks) {
        super(dataGenerator.getPackOutput(), new HashSet<>(), new ArrayList<>());
        this.blocksToProcess = blocks;
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return Collections.singletonList(
            new SubProviderEntry(this::createBlockLootTables, LootContextParamSets.BLOCK)
        );
    }

    protected BasicBlockLootTables createBlockLootTables() {
        return new BasicBlockLootTables(blocksToProcess);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        //super.validate(map, validationtracker);
    }


}
