/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium._impl.TagConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagUtil {

    public static <T> boolean hasTag(T type, SetTag<T> tag) {
        return tag.contains(type); //contains
    }

    public static TagCollection<Block> getAllBlockTags(Level world) {
        return world.getTagManager().getOrEmpty(Registry.BLOCK_REGISTRY);
    }

    public static TagCollection<Item> getAllItemTags(Level world) {
        return world.getTagManager().getOrEmpty(Registry.ITEM_REGISTRY);
    }

    public static TagCollection<Fluid> getAllFluidTags(Level world) {
        return world.getTagManager().getOrEmpty(Registry.FLUID_REGISTRY);
    }

    public static <T> Collection<T> getAllEntries(SetTag<T>... tags) {
        if (tags.length == 0)
            return Collections.emptyList();
        if (tags.length == 1)
            return tags[0].getValues(); //getAllElements
        List<T> list = new ArrayList<>();
        for (SetTag<T> tag : tags) {
            list.addAll(tag.getValues()); //getAllElements
        }
        return list;
    }

    public static <T> Collection<T> getAllEntries(SetTag<T> tag) {
        return tag.getValues();
    } //getAllElements

    public static <T> Tag<T> getOrCreateTag(TagCollection<T> collection, ResourceLocation resourceLocation) {
        if (collection.getAvailableTags().contains(resourceLocation)) {
            return collection.getTag(resourceLocation);
        }
        return collection.getTagOrEmpty(resourceLocation);
    }

    public static Tag<Item> getItemTag(ResourceLocation resourceLocation) {
        if (ItemTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return ItemTags.getAllTags().getTag(resourceLocation);
        }
        return ItemTags.bind(resourceLocation.toString());
    }

    public static Tag<Block> getBlockTag(ResourceLocation resourceLocation) {
        if (BlockTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return BlockTags.getAllTags().getTag(resourceLocation);
        }
        return BlockTags.bind(resourceLocation.toString());
    }

    public static Tag<EntityType<?>> getEntityTypeTag(ResourceLocation resourceLocation) {
        if (EntityTypeTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return EntityTypeTags.getAllTags().getTag(resourceLocation);
        }
        return EntityTypeTags.bind(resourceLocation.toString());
    }

    public static Tag<Fluid> getFluidTag(ResourceLocation resourceLocation) {
        if (FluidTags.getAllTags().getAvailableTags().contains(resourceLocation)) {
            return FluidTags.getAllTags().getTag(resourceLocation);
        }
        return FluidTags.bind(resourceLocation.toString());
    }

    public static ItemStack getItemWithPreference(Tag<Item> tag){
        if (tag.getValues().isEmpty()) return ItemStack.EMPTY;
        List<Item> elements = tag.getValues();
        for (String modid : TagConfig.ITEM_PREFERENCE) {
            for (Item allElement : elements) {
                if (allElement.getRegistryName().getNamespace().equalsIgnoreCase(modid)) return new ItemStack(allElement);
            }
        }
        return new ItemStack(elements.get(0));
    }
}
