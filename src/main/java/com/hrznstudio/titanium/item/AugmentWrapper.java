/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

public class AugmentWrapper {

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Map<String, Float>>> ATTACHMENT;

    public static boolean isAugment(ItemStack augment) {
        if (!augment.isEmpty()) {
            return augment.has(ATTACHMENT);
        }
        return false;
    }

    public static Map<String, Float> getAugment(ItemStack augment) {
        if (!augment.isEmpty()) {
            return augment.getOrDefault(ATTACHMENT, Map.of());
        }
        return Map.of();
    }

    public static boolean hasType(ItemStack augment, IAugmentType type) {
        var augmentCache = getAugment(augment);
        return augmentCache != null && augmentCache.containsKey(type.getType());
    }

    public static float getType(ItemStack augment, IAugmentType type) {
        var augmentCache = getAugment(augment);
        return augmentCache != null ? augmentCache.get(type.getType()) : 0f;
    }

    public static void setType(ItemStack augment, IAugmentType type, float amount) {
        var newMap = new HashMap<>(getAugment(augment));
        newMap.put(type.getType(), amount);
        augment.set(ATTACHMENT, newMap);
    }
}
