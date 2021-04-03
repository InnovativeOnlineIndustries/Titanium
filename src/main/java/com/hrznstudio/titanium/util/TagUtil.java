/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium._impl.TagConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagUtil {

    public static <T> boolean hasTag(T type, Tag<T> tag) {
        return tag.contains(type); //contains
    }

    public static ITagCollection<Block> getAllBlockTags(World world) {
        return world.getTags().getBlockTags();
    }

    public static ITagCollection<Item> getAllItemTags(World world) {
        return world.getTags().getItemTags();
    }

    public static ITagCollection<Fluid> getAllFluidTags(World world) {
        return world.getTags().getFluidTags();
    }

    public static <T> Collection<T> getAllEntries(Tag<T>... tags) {
        if (tags.length == 0)
            return Collections.emptyList();
        if (tags.length == 1)
            return tags[0].getAllElements(); //getAllElements
        List<T> list = new ArrayList<>();
        for (Tag<T> tag : tags) {
            list.addAll(tag.getAllElements()); //getAllElements
        }
        return list;
    }

    public static <T> Collection<T> getAllEntries(Tag<T> tag) {
        return tag.getAllElements();
    } //getAllElements

    public static <T> ITag<T> getOrCreateTag(ITagCollection<T> collection, ResourceLocation resourceLocation) {
        if (collection.getRegisteredTags().contains(resourceLocation)) {
            return collection.get(resourceLocation);
        }
        return collection.getTagByID(resourceLocation);
    }

    public static ITag<Item> getItemTag(ResourceLocation resourceLocation) {
        if (ItemTags.getCollection().getRegisteredTags().contains(resourceLocation)) {
            return ItemTags.getCollection().get(resourceLocation);
        }
        return ItemTags.makeWrapperTag(resourceLocation.toString());
    }

    public static ITag<Block> getBlockTag(ResourceLocation resourceLocation) {
        if (BlockTags.getCollection().getRegisteredTags().contains(resourceLocation)) {
            return BlockTags.getCollection().get(resourceLocation);
        }
        return BlockTags.makeWrapperTag(resourceLocation.toString());
    }

    public static ITag<EntityType<?>> getEntityTypeTag(ResourceLocation resourceLocation) {
        if (EntityTypeTags.getCollection().getRegisteredTags().contains(resourceLocation)) {
            return EntityTypeTags.getCollection().get(resourceLocation);
        }
        return EntityTypeTags.getTagById(resourceLocation.toString());
    }

    public static ITag<Fluid> getFluidTag(ResourceLocation resourceLocation) {
        if (FluidTags.getCollection().getRegisteredTags().contains(resourceLocation)) {
            return FluidTags.getCollection().get(resourceLocation);
        }
        return FluidTags.makeWrapperTag(resourceLocation.toString());
    }

    public static ItemStack getItemWithPreference(ITag<Item> tag){
        if (tag.getAllElements().isEmpty()) return ItemStack.EMPTY;
        List<Item> elements = tag.getAllElements();
        for (String modid : TagConfig.ITEM_PREFERENCE) {
            for (Item allElement : elements) {
                if (allElement.getRegistryName().getNamespace().equalsIgnoreCase(modid)) return new ItemStack(allElement);
            }
        }
        return new ItemStack(elements.get(0));
    }
}
