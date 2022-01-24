/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.itemstack.ItemStackHarnessRegistry;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.network.locator.PlayerInventoryFinder;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class InventoryStackLocatorInstance extends LocatorInstance {

    private int inventorySlot;
    private String finder;

    public InventoryStackLocatorInstance() {
        this("empty", 0);
    }

    public InventoryStackLocatorInstance(String finder, int inventorySlot) {
        super(LocatorTypes.INVENTORY_LOCATOR);
        this.inventorySlot = inventorySlot;
        this.finder = finder;
    }

    @Override
    public Optional<?> locale(Player playerEntity) {
        return PlayerInventoryFinder.get(finder).map(playerInventoryFinder -> playerInventoryFinder.getStackGetter().apply(playerEntity, inventorySlot))
                .map(ItemStackHarnessRegistry::createItemStackHarness).orElseGet(null);
    }

    public int getInventorySlot() {
        return inventorySlot;
    }
}
