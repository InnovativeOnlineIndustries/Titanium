/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class TitaniumSerializableProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    protected final DataGenerator generator;
    private final Map<IJsonFile, IJSONGenerator> serializables;
    private final String modid;

    public TitaniumSerializableProvider(DataGenerator generatorIn, String modid) {
        this.generator = generatorIn;
        this.modid = modid;
        this.serializables = new HashMap<>();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        add(serializables);
        Path path = this.generator.getPackOutput().getOutputFolder();
        Set<Path> set = Sets.newHashSet();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        serializables.forEach((iJsonFile, ijsonGenerator) -> {
            Path outputFile = path.resolve("data/" + modid + "/recipes/" + (iJsonFile.getRecipeSubfolder() != null ? iJsonFile.getRecipeSubfolder() + "/" : "") + iJsonFile.getRecipeKey() + ".json");
            if (!set.add(outputFile)) {
                throw new IllegalStateException("Duplicate recipe " + iJsonFile.getRecipeKey());
            } else {
                futures.add(DataProvider.saveStable(cache, ijsonGenerator.generate(), outputFile));
            }
        });
        return CompletableFuture.allOf(futures.toArray((i) -> new CompletableFuture<?>[i]));
    }

    public abstract void add(Map<IJsonFile, IJSONGenerator> serializables);

    @Override
    public String getName() {
        return "Titanium Serializable (" + modid + ")";
    }
}
