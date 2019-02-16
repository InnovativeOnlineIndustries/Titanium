package com.hrznstudio.titanium.api.augment;

import com.hrznstudio.titanium.api.IMachine;
import net.minecraft.util.IItemProvider;

public interface IAugment extends IItemProvider {

    IAugmentType getAugmentType();

    float getAugmentRatio();

    boolean canWorkIn(IMachine machine);
}
