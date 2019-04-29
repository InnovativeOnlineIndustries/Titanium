/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.util;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TagUtil {
    public static <T> boolean hasTag(T type, Tag<T> tag) {
        return tag.contains(type);
    }

    public static TagCollection<Block> getAllBlockTags(World world) {
        return world.getTags().getBlocks();
    }

    public static TagCollection<Item> getAllItemTags(World world) {
        return world.getTags().getItems();
    }

    public static TagCollection<Fluid> getAllFluidTags(World world) {
        return world.getTags().getFluids();
    }

    public static <T> Collection<T> getAllEntries(Tag<T>... tags) {
        if (tags.length == 0)
            return Collections.emptyList();
        if (tags.length == 1)
            return tags[0].getAllElements();
        List<T> list = new ArrayList<>();
        for (Tag<T> tag : tags) {
            list.addAll(tag.getAllElements());
        }
        return list;
    }

    public static <T> Collection<T> getAllEntries(Tag<T> tag) {
        return tag.getAllElements();
    }
}