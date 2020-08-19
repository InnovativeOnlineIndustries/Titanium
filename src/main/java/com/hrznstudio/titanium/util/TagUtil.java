/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.Tag;
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
        return world.getTags().func_241835_a();
    }

    public static ITagCollection<Item> getAllItemTags(World world) {
        return world.getTags().func_241836_b();
    }

    public static ITagCollection<Fluid> getAllFluidTags(World world) {
        return world.getTags().func_241837_c();
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
}
