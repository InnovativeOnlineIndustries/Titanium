/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */
package com.hrznstudio.titanium.block.tile.progress;

import net.minecraft.world.World;

//TODO This works but can be made further generic
public interface IProgressing {
    void markForUpdate();

    World getWorld();
}
