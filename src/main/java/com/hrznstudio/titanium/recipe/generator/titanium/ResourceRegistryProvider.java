/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceRegistryProvider implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator generator;
    private ItemTagsProvider itemTagsProvider;
    private BlockTagsProvider blockTagsProvider;

    public ResourceRegistryProvider(DataGenerator generator, String modid, ExistingFileHelper helper) {
        this.generator = generator;
        Map<ResourceLocation, List<Block>> typeBlockMap = new HashMap<>();
        this.blockTagsProvider = new BlockTagsProvider(generator, modid, helper) {
            @Override
            protected void registerTags() {
                ResourceRegistry.getMaterials().forEach(material -> {
                    material.getGenerated().values().stream().filter(entry -> entry instanceof IResourceHolder).forEach(entry -> {
                        IResourceHolder resourceHolder = ((IResourceHolder) entry);
                        String tag = resourceHolder.getType().getTag();
                        String type = resourceHolder.getMaterial().getMaterialType();
                        if (entry instanceof Block) {
                            this.getOrCreateBuilder(BlockTags.makeWrapperTag("forge:" + tag + "/" + type)).add((Block) entry);//Builder, add
                            typeBlockMap.compute(new ResourceLocation("forge", tag), (resourceLocation, blocks) -> {
                                if (blocks == null) {
                                    List<Block> list = NonNullList.create();
                                    list.add((Block) entry);
                                    return list;
                                } else {
                                    blocks.add((Block) entry);
                                    return blocks;
                                }
                            });
                        }
                    });
                });
                typeBlockMap.forEach((tagLocation, blockList) -> this.getOrCreateBuilder(BlockTags.makeWrapperTag(tagLocation.toString())).add(blockList.toArray(new Block[blockList.size()])));//Builder, add
            }
        };
        this.itemTagsProvider = new ItemTagsProvider(generator, blockTagsProvider, modid, helper) {
            @Override
            protected void registerTags() {
                Map<ResourceLocation, List<Item>> typeItemMap = new HashMap<>();
                ResourceRegistry.getMaterials().forEach(material -> material.getGenerated().values().stream().filter(entry -> entry instanceof IResourceHolder).forEach(entry -> {
                    IResourceHolder resourceHolder = ((IResourceHolder) entry);
                    String tag = resourceHolder.getType().getTag();
                    String type = resourceHolder.getMaterial().getMaterialType();
                    if (entry instanceof Block) {
                        this.copy(BlockTags.createOptional(new ResourceLocation("forge:" + tag + "/" + type)), //copy
                            ItemTags.createOptional(new ResourceLocation("forge:" + tag + "/" + type)));
                        typeBlockMap.compute(new ResourceLocation("forge", tag), (resourceLocation, blocks) -> {
                            if (blocks == null) {
                                List<Block> list = NonNullList.create();
                                list.add((Block) entry);
                                return list;
                            } else {
                                blocks.add((Block) entry);
                                return blocks;
                            }
                        });
                    } else if (entry instanceof Item) {
                        this.getOrCreateBuilder(ItemTags.createOptional(new ResourceLocation("forge:" + tag + "/" + type))).add((Item) entry); //Builder, add
                        typeItemMap.compute(new ResourceLocation("forge", tag), (resourceLocation, items) -> {
                            if (items == null) {
                                List<Item> list = NonNullList.create();
                                list.add((Item) entry);
                                return list;
                            } else {
                                items.add((Item) entry);
                                return items;
                            }
                        });
                    }
                }));
                typeItemMap.forEach((tagLocation, itemList) -> this.getOrCreateBuilder(ItemTags.createOptional(tagLocation)).add(itemList.toArray(new Item[itemList.size()]))); //Builder, add
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
