/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component;

import net.minecraft.world.World;

public interface IComponentHarness {

    World getComponentWorld();

    /**
     * Allows
     * @param referenced
     */
    void markComponentForUpdate(boolean referenced);

    void markComponentDirty();

}
