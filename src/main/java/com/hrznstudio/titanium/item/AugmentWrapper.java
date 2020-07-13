/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class AugmentWrapper {

    public static final String AUGMENT_NBT = "TitaniumAugment";

    private final ItemStack augment;

    public AugmentWrapper(ItemStack augment) {
        this.augment = augment;
    }

    public boolean isAugment() {
        return this.augment.hasTag() && this.augment.getTag().contains(AUGMENT_NBT);
    }

    public boolean hasType(IAugmentType type) {
        return isAugment() && this.augment.getTag().getCompound(AUGMENT_NBT).contains(type.getType());
    }

    public float getType(IAugmentType type) {
        return hasType(type) ? this.augment.getTag().getCompound(AUGMENT_NBT).getFloat(type.getType()) : 0f;
    }

    public AugmentWrapper setType(IAugmentType type, float amount) {
        CompoundNBT nbt = this.augment.getOrCreateTag();
        CompoundNBT augmentNBT = nbt.contains(AUGMENT_NBT) ? nbt.getCompound(AUGMENT_NBT) : new CompoundNBT();
        augmentNBT.putFloat(type.getType(), amount);
        nbt.put(AUGMENT_NBT, augmentNBT);
        this.augment.setTag(nbt);
        return this;
    }
}
