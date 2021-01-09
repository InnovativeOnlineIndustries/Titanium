/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator;

import com.hrznstudio.titanium.network.locator.instance.EmptyLocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.HeldStackLocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.InventoryStackLocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;

public class LocatorTypes {
    public static final LocatorType TILE_ENTITY = new LocatorType("tile_entity", TileEntityLocatorInstance::new);
    public static final LocatorType HELD_STACK = new LocatorType("held_stack", HeldStackLocatorInstance::new);
    public static final LocatorType EMPTY = new LocatorType("empty", EmptyLocatorInstance::new);
    public static final LocatorType INVENTORY_LOCATOR = new LocatorType("inventory_slot", InventoryStackLocatorInstance::new);


    public static void register() {
        LocatorFactory.registerLocatorType(TILE_ENTITY);
        LocatorFactory.registerLocatorType(HELD_STACK);
        LocatorFactory.registerLocatorType(EMPTY);
        LocatorFactory.registerLocatorType(INVENTORY_LOCATOR);
    }
}
