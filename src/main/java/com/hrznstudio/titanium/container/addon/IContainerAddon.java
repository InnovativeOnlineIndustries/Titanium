/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public interface IContainerAddon {
    default List<Slot> getSlots() {
        return Lists.newArrayList();
    }

    default List<DataSlot> getIntReferenceHolders() {
        return Lists.newArrayList();
    }

    default List<ContainerData> getIntArrayReferenceHolders() {
        return Lists.newArrayList();
    }
}
