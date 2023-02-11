/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component;

import net.minecraft.world.level.Level;


public interface IComponentHarness {

    Level getComponentWorld();

    /**
     * Allows a Component to force an updated
     * @param referenced whether or not this update would be covered by an IntReferenceHolder in a container, AKA should I sync this, if I don't need it outside a container.
     */
    void markComponentForUpdate(boolean referenced);

    void markComponentDirty();

}
