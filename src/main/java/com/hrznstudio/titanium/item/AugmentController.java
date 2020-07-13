package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.augment.IAugmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class AugmentController {

    public static final String AUGMENT_NBT = "TitaniumAugment";

    private final ItemStack augment;

    public AugmentController(ItemStack augment) {
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

    public AugmentController setType(IAugmentType type, float amount) {
        CompoundNBT nbt = this.augment.getOrCreateTag();
        CompoundNBT augmentNBT = nbt.contains(AUGMENT_NBT) ? nbt.getCompound(AUGMENT_NBT) : new CompoundNBT();
        augmentNBT.putFloat(type.getType(), amount);
        nbt.put(AUGMENT_NBT, augmentNBT);
        this.augment.setTag(nbt);
        return this;
    }
}
