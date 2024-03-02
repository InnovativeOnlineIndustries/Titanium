/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class AugmentWrapper {

    public static final String AUGMENT_NBT = "TitaniumAugment";

    public static boolean isAugment(ItemStack augment) {
        return augment.hasTag() && augment.getTag().contains(AUGMENT_NBT);
    }

    public static boolean hasType(ItemStack augment, IAugmentType type) {
        return isAugment(augment) && augment.getTag().getCompound(AUGMENT_NBT).contains(type.getType());
    }

    public static float getType(ItemStack augment, IAugmentType type) {
        return hasType(augment, type) ? augment.getTag().getCompound(AUGMENT_NBT).getFloat(type.getType()) : 0f;
    }

    public static void setType(ItemStack augment, IAugmentType type, float amount) {
        CompoundTag nbt = augment.getOrCreateTag();
        CompoundTag augmentNBT = nbt.contains(AUGMENT_NBT) ? nbt.getCompound(AUGMENT_NBT) : new CompoundTag();
        augmentNBT.putFloat(type.getType(), amount);
        nbt.put(AUGMENT_NBT, augmentNBT);
        augment.setTag(nbt);
    }
}
