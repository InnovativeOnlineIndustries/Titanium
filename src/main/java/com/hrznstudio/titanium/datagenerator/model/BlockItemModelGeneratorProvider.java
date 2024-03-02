/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.datagenerator.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BlockItemModelGeneratorProvider implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;
    private final String modid;
    private final NonNullLazy<List<Block>> blocksToProcess;

    public BlockItemModelGeneratorProvider(DataGenerator generator, String modid, NonNullLazy<List<Block>> blocksToProcess) {
        this.generator = generator;
        this.modid = modid;
        this.blocksToProcess = blocksToProcess;
    }

    private static JsonObject createModel(Block block) {
        JsonObject object = new JsonObject();
        ResourceLocation blockRL = ForgeRegistries.BLOCKS.getKey(block);
        object.addProperty("parent", blockRL.getNamespace() + ":block/" + blockRL.getPath());
        return object;
    }

    @Override
    public void run(@Nonnull CachedOutput cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Path output = path.resolve("assets/" + modid + "/models/item/");
        Files.createDirectories(output);
        blocksToProcess.get().forEach(blockBase -> {
            try {
                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(output.resolve(ForgeRegistries.BLOCKS.getKey(blockBase).getPath() + ".json"))) {
                    bufferedwriter.write(GSON.toJson(createModel(blockBase)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    @Nonnull
    public String getName() {
        return "Block Model Item Generator (" + modid + ")";
    }
}
