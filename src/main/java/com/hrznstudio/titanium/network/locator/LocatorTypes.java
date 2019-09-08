package com.hrznstudio.titanium.network.locator;

import com.hrznstudio.titanium.network.locator.instance.HeldStackLocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;

public class LocatorTypes {
    public static final LocatorType TILE_ENTITY = new LocatorType("tile_entity", TileEntityLocatorInstance::new);
    public static final LocatorType HELD_STACK = new LocatorType("held_stack", HeldStackLocatorInstance::new);

    public static void register() {
        LocatorFactory.registerLocatorType(TILE_ENTITY);
        LocatorFactory.registerLocatorType(HELD_STACK);
    }
}
