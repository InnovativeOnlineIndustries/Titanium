/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AugmentWrapper {

    public static HashMap<Integer, AugmentCache> STACK_AUGMENT_CACHE = new LinkedHashMap<>();

    public static final String AUGMENT_NBT = "TitaniumAugment";

    public static boolean isAugment(ItemStack augment) {
        if (!augment.isEmpty()) {
            var hash = augment.hashCode();
            if (STACK_AUGMENT_CACHE.containsKey(hash)) {
                return true;
            } else if (augment.hasTag() && augment.getTag().contains(AUGMENT_NBT)) {
                STACK_AUGMENT_CACHE.put(hash, new AugmentCache(augment));
                return true;
            }
        }
        return false;
    }

    public static AugmentCache getAugment(ItemStack augment) {
        if (!augment.isEmpty()) {
            var hash = augment.hashCode();
            if (STACK_AUGMENT_CACHE.containsKey(hash)) {
                return STACK_AUGMENT_CACHE.get(hash);
            } else if (augment.hasTag() && augment.getTag().contains(AUGMENT_NBT)) {
                return STACK_AUGMENT_CACHE.put(hash, new AugmentCache(augment));
            }
        }
        return null;
    }

    public static boolean hasType(ItemStack augment, IAugmentType type) {
        var augmentCache = getAugment(augment);
        return augmentCache != null && augmentCache.hasAugment(type);
    }

    public static float getType(ItemStack augment, IAugmentType type) {
        var augmentCache = getAugment(augment);
        return augmentCache != null ? augmentCache.getAugment(type) : 0f;
    }

    public static void setType(ItemStack augment, IAugmentType type, float amount) {
        CompoundTag nbt = augment.getOrCreateTag();
        CompoundTag augmentNBT = nbt.contains(AUGMENT_NBT) ? nbt.getCompound(AUGMENT_NBT) : new CompoundTag();
        augmentNBT.putFloat(type.getType(), amount);
        nbt.put(AUGMENT_NBT, augmentNBT);
        augment.setTag(nbt);
    }

    private static class AugmentCache {

        private HashMap<String, Float> augmentValues;

        public AugmentCache(ItemStack stack) {
            this.augmentValues = new LinkedHashMap<>();
            var compound = stack.getTag().getCompound(AUGMENT_NBT);
            for (String allKey : compound.getAllKeys()) {
                this.augmentValues.put(allKey, compound.getFloat(allKey));
            }
        }

        public boolean hasAugment(IAugmentType type) {
            return this.augmentValues.containsKey(type.getType());
        }

        public float getAugment(IAugmentType type) {
            return this.augmentValues.get(type.getType());
        }
    }
}
