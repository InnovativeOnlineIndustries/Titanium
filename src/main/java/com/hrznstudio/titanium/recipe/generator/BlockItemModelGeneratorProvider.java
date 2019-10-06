/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hrznstudio.titanium.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BlockItemModelGeneratorProvider implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator generator;
    private final String modid;

    public BlockItemModelGeneratorProvider(DataGenerator generator, String modid) {
        this.generator = generator;
        this.modid = modid;
    }

    private static JsonObject createModel(Block block) {
        JsonObject object = new JsonObject();
        object.addProperty("parent", block.getRegistryName().getNamespace() + ":block/" + block.getRegistryName().getPath());
        return object;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Path output = path.resolve("assets/" + modid + "/models/item/");
        Files.createDirectories(output);
        BlockBase.BLOCKS.stream().filter(blockBase -> blockBase.getRegistryName().getNamespace().equals(modid)).forEach(blockBase -> {
            try {
                try (BufferedWriter bufferedwriter = Files.newBufferedWriter(output.resolve(blockBase.getRegistryName().getPath() + ".json"))) {
                    bufferedwriter.write(GSON.toJson(createModel(blockBase)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getName() {
        return "Block Model Item Generator (" + modid + ")";
    }
}
