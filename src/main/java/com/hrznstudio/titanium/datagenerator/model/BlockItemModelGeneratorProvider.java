/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class BlockItemModelGeneratorProvider implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    private final String modid;
    private final Supplier<List<Block>> blocksToProcess;

    public BlockItemModelGeneratorProvider(DataGenerator generator, String modid, Supplier<List<Block>> blocksToProcess) {
        this.generator = generator;
        this.modid = modid;
        this.blocksToProcess = blocksToProcess;
    }

    private static JsonObject createModel(Block block) {
        JsonObject object = new JsonObject();
        Identifier blockRL = BuiltInRegistries.BLOCK.getKey(block);
        object.addProperty("parent", blockRL.getNamespace() + ":block/" + blockRL.getPath());
        return object;
    }

    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        Path path = this.generator.getPackOutput().getOutputFolder();
        Path output = path.resolve("assets/" + modid + "/models/item/");
        try {
            Files.createDirectories(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<CompletableFuture<?>> futures = new ArrayList<>();
        blocksToProcess.get().forEach(blockBase -> {
            futures.add(CompletableFuture.runAsync(() -> {
                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(output.resolve(BuiltInRegistries.BLOCK.getKey(blockBase).getPath() + ".json"))) {
                    bufferedwriter.write(GSON.toJson(createModel(blockBase)));
                } catch (Exception e) {

                }
            }));
        });
        return CompletableFuture.allOf(futures.toArray((i) -> new CompletableFuture<?>[i]));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Block Model Item Generator (" + modid + ")";
    }
}
