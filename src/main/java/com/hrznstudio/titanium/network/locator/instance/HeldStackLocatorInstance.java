/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.itemstack.ItemStackHarnessRegistry;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Optional;

public class HeldStackLocatorInstance extends LocatorInstance {
    private boolean mainHand;

    public HeldStackLocatorInstance() {
        this(true);
    }

    public HeldStackLocatorInstance(boolean mainHand) {
        super(LocatorTypes.HELD_STACK);
        this.mainHand = mainHand;
    }

    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return Optional.of(playerEntity.getHeldItem(mainHand ? Hand.MAIN_HAND : Hand.OFF_HAND))
            .map(ItemStackHarnessRegistry::createItemStackHarness).orElseGet(null);
    }

    public boolean isMainHand() {
        return mainHand;
    }
}
