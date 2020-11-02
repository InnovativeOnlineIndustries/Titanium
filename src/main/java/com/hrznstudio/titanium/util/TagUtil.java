/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
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
}
