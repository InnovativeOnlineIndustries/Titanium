/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator.titanium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.titanium.api.material.IResourceHolder;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceRegistryProvider implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator generator;
    private ItemTagsProvider itemTagsProvider;
    private BlockTagsProvider blockTagsProvider;

    public ResourceRegistryProvider(DataGenerator generator) {
        this.generator = generator;
        this.itemTagsProvider = new ItemTagsProvider(generator) {
            @Override
            protected void registerTags() {
                ResourceRegistry.getMaterials().forEach(material -> {
                    material.getGenerated().values().stream().filter(entry -> entry instanceof IResourceHolder).forEach(entry -> {
                        if (entry instanceof Block) {
                            this.copy(new BlockTags.Wrapper(new ResourceLocation("forge", ((IResourceHolder) entry).getType().getTag() + "/" + ((IResourceHolder) entry).getMaterial().getMaterialType())),
                                    new ItemTags.Wrapper(new ResourceLocation("forge", ((IResourceHolder) entry).getType().getTag() + "/" + ((IResourceHolder) entry).getMaterial().getMaterialType())));
                        } else if (entry instanceof Item) {
                            this.getBuilder(new ItemTags.Wrapper(new ResourceLocation("forge", ((IResourceHolder) entry).getType().getTag() + "/" + ((IResourceHolder) entry).getMaterial().getMaterialType()))).add((Item) entry);
                        }
                    });
                });
            }
        };
        this.blockTagsProvider = new BlockTagsProvider(generator) {
            @Override
            protected void registerTags() {
                ResourceRegistry.getMaterials().forEach(material -> {
                    material.getGenerated().values().stream().filter(entry -> entry instanceof IResourceHolder).forEach(entry -> {
                        if (entry instanceof Block) {
                            this.getBuilder(new BlockTags.Wrapper(new ResourceLocation("forge", ((IResourceHolder) entry).getType().getTag() + "s/" + ((IResourceHolder) entry).getMaterial().getMaterialType()))).add((Block) entry);
                        }
                    });
                });
            }
        };
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        this.itemTagsProvider.act(cache);
        this.blockTagsProvider.act(cache);
        Path path = this.generator.getOutputFolder();
        ResourceRegistry.getMaterials().forEach(material -> {
            material.getGenerated().values().stream().filter(entry -> entry instanceof IJSONGenerator && entry instanceof IJsonFile).forEach(entry -> {
                Path output = path.resolve((((IJsonFile) entry).getRecipeSubfolder() != null ? ((IJsonFile) entry).getRecipeSubfolder() : ""));
                try {
                    Files.createDirectories(output);
                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(output.resolve(((IJsonFile) entry).getRecipeKey() + ".json"))) {
                        bufferedwriter.write(GSON.toJson(((IJSONGenerator) entry).generate()));
                    }
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            });
        });
    }

    @Override
    public String getName() {
        return "Resource Registry";
    }
}
