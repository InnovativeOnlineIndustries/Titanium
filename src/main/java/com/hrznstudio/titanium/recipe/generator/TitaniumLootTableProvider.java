/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class TitaniumLootTableProvider extends LootTableProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private final DataGenerator generator;

    public TitaniumLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    public void createSimple(Block block) {
        lootTables.put(block, LootTable.builder().addLootPool(LootPool.builder().name(block.getRegistryName().getPath()).rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block))));
    }

    public void createEmpty(Block block) {
        lootTables.put(block, LootTable.builder());
    }

    @Override
    public void act(DirectoryCache cache) {
        add();
        lootTables.forEach((block, builder) -> write(cache, block.getRegistryName(), builder.setParameterSet(LootParameterSets.BLOCK).build()));
    }

    private void write(DirectoryCache cache, ResourceLocation resourceLocation, LootTable table) {
        Path output = this.generator.getOutputFolder().resolve("data/" + resourceLocation.getNamespace() + "/loot_tables/blocks/" + resourceLocation.getPath() + ".json");
        try {
            IDataProvider.save(GSON, cache, LootTableManager.toJson(table), output);
        } catch (IOException e) {
            LOGGER.error("Couldn't write loot table {}", output, e);
        }
    }

    @Override
    public String getName() {
        return "Titanium Loot Table";
    }

    public abstract void add();
}
