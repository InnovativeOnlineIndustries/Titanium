/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;

import java.util.List;

public interface IContainerAddon {
    default List<Slot> getSlots() {
        return Lists.newArrayList();
    }

    default List<IntReferenceHolder> getIntReferenceHolders() {
        return Lists.newArrayList();
    }

    default List<IIntArray> getIntArrayReferenceHolders() {
        return Lists.newArrayList();
    }
}
