/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium._impl.TagConfig;
import net.minecraft.core.Registry;
import net.minecraft.nbt.TagType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagUtil {

    public static <T extends ForgeRegistryEntry<T>> boolean hasTag(IForgeRegistry<T> registry, T type, TagKey<T> tag) {
        return registry.tags().getTag(tag).contains(type);
    }

    public static ITagManager<Block> getAllBlockTags() {
        return ForgeRegistries.BLOCKS.tags();
    }

    public static ITagManager<Item> getAllItemTags() {
        return ForgeRegistries.ITEMS.tags();
    }

    public static ITagManager<Fluid> getAllFluidTags() {
        return ForgeRegistries.FLUIDS.tags();
    }

    public static <T extends ForgeRegistryEntry<T>> Collection<T> getAllEntries(IForgeRegistry<T> registry, TagKey<T>... tags) {
        if (tags.length == 0)
            return Collections.emptyList();
        if (tags.length == 1)
            return registry.tags().getTag(tags[0]).stream().toList(); //getAllElements
        List<T> list = new ArrayList<>();
        for (TagKey<T> tag : tags) {
            list.addAll(registry.tags().getTag(tag).stream().toList()); //getAllElements
        }
        return list;
    }

    public static <T extends ForgeRegistryEntry<T>> Collection<T> getAllEntries(IForgeRegistry<T> registry, TagKey<T> tag) {
        return registry.tags().getTag(tag).stream().toList();
    }

    public static <T extends ForgeRegistryEntry<T>> TagKey<T> getOrCreateTag(IForgeRegistry<T> registry, ResourceLocation resourceLocation) {
        /*
        if (registry.tags().stream().anyMatch(ts -> ts.getKey().location().equals(resourceLocation))) {

        }
        return collection.getTagOrEmpty(resourceLocation);
        */
        return registry.tags().createTagKey(resourceLocation);
    }

    public static TagKey<Item> getItemTag(ResourceLocation resourceLocation) {
        /*
        if (ItemTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return ItemTags.getAllTags().getTag(resourceLocation);
        }
        return ItemTags.create(resourceLocation);
        */

        return ForgeRegistries.ITEMS.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.ITEMS, resourceLocation));
    }

    public static TagKey<Block> getBlockTag(ResourceLocation resourceLocation) {
        /*if (BlockTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return BlockTags.getAllTags().getTag(resourceLocation);
        }
        return BlockTags.create(resourceLocation);*/
        return ForgeRegistries.BLOCKS.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.BLOCKS, resourceLocation));

    }

    public static TagKey<EntityType<?>> getEntityTypeTag(ResourceLocation resourceLocation) {
        /*if (EntityTypeTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return EntityTypeTags.getAllTags().getTag(resourceLocation);
        }
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, resourceLocation);*/
        return ForgeRegistries.ENTITIES.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.ENTITIES, resourceLocation));

    }

    public static TagKey<Fluid> getFluidTag(ResourceLocation resourceLocation) {
        /*if (FluidTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return FluidTags.getAllTags().getTag(resourceLocation);
        }
        Registry.FLUID_REGISTRY.cast()
        return FluidTags.create(resourceLocation);*/
        return ForgeRegistries.FLUIDS.tags().stream().filter(items -> items.getKey().location().equals(resourceLocation)).map(ITag::getKey).findFirst().orElse(getOrCreateTag(ForgeRegistries.FLUIDS, resourceLocation));

    }

    public static ItemStack getItemWithPreference(TagKey<Item> tagKey){
        ITag<Item> item = ForgeRegistries.ITEMS.tags().getTag(tagKey);
        if (item.isEmpty()) return ItemStack.EMPTY;
        List<Item> elements = item.stream().toList();
        for (String modid : TagConfig.ITEM_PREFERENCE) {
            for (Item allElement : elements) {
                if (allElement.getRegistryName().getNamespace().equalsIgnoreCase(modid)) return new ItemStack(allElement);
            }
        }
        return new ItemStack(elements.get(0));
    }
}
