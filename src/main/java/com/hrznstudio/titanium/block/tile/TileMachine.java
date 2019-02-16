/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.api.IMachine;
import com.hrznstudio.titanium.api.augment.IAugment;
import com.hrznstudio.titanium.api.augment.IAugmentType;
import com.hrznstudio.titanium.block.BlockMachine;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileMachine extends TilePowered implements ITickable, IMachine {
    public TileMachine(BlockMachine<?> blockMachine) {
        super(blockMachine);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean canAcceptAugment(IAugment augment) {
        return false;
    }

    @Override
    public List<IAugment> getInstalledAugments() {
        return null;
    }

    @Override
    public List<IAugment> getInstalledAugments(IAugmentType filter) {
        return null;
    }
}
