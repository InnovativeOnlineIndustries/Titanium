/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import com.hrznstudio.titanium._impl.TagConfig;
import com.hrznstudio.titanium.compat.almostunified.AlmostUnifiedAdapter;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

public class TagUtil {

    public static <T> boolean hasTag(Registry<T> registry, T type, TagKey<T> tag) {
        return StreamSupport.stream(registry.getTagOrEmpty(tag).spliterator(), false).anyMatch(h -> h.value() == type);
    }

    public static <T> Collection<T> getAllEntries(Registry<T> registry, TagKey<T>... tags) {
        if (tags.length == 0)
            return Collections.emptyList();
        if (tags.length == 1)
            return getAllEntries(registry, tags[0]); //getAllElements
        List<T> list = new ArrayList<>();
        for (TagKey<T> tag : tags) {
            list.addAll(getAllEntries(registry, tag)); //getAllElements
        }
        return list;
    }

    public static <T> Collection<T> getAllEntries(Registry<T> registry, TagKey<T> tag) {
        return StreamSupport.stream(registry.getTagOrEmpty(tag).spliterator(), false).map(Holder::value).toList();
    }

    public static <T> TagKey<T> getOrCreateTag(Registry<T> registry, ResourceLocation resourceLocation) {
        return TagKey.create(registry.key(), resourceLocation);
    }

    public static TagKey<Item> getItemTag(ResourceLocation resourceLocation) {
        return getOrCreateTag(BuiltInRegistries.ITEM, resourceLocation);
    }

    public static TagKey<Block> getBlockTag(ResourceLocation resourceLocation) {
        return getOrCreateTag(BuiltInRegistries.BLOCK, resourceLocation);
    }

    public static TagKey<EntityType<?>> getEntityTypeTag(ResourceLocation resourceLocation) {
        return getOrCreateTag(BuiltInRegistries.ENTITY_TYPE, resourceLocation);

    }

    public static TagKey<Fluid> getFluidTag(ResourceLocation resourceLocation) {
        return getOrCreateTag(BuiltInRegistries.FLUID, resourceLocation);
    }

    public static ItemStack getItemWithPreference(TagKey<Item> tagKey) {
        Item preferredItem = AlmostUnifiedAdapter.getPreferredItemForTag(tagKey);
        if (preferredItem != null) {
            return new ItemStack(preferredItem);
        }

        var item = BuiltInRegistries.ITEM.getTag(tagKey);
        if (item.isEmpty()) return ItemStack.EMPTY;
        List<Item> elements = item.get().stream().map(Holder::value).toList();
        for (String modid : TagConfig.ITEM_PREFERENCE) {
            for (Item allElement : elements) {
                if (BuiltInRegistries.ITEM.getKey(allElement).getNamespace().equalsIgnoreCase(modid)) return new ItemStack(allElement);
            }
        }
        return new ItemStack(elements.get(0));
    }
}
