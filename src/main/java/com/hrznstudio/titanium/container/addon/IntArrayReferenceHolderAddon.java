/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.world.inventory.ContainerData;

import java.util.List;


public class IntArrayReferenceHolderAddon implements IContainerAddon {
    private final ContainerData[] referenceHolders;

    public IntArrayReferenceHolderAddon(ContainerData... referenceHolders) {
        this.referenceHolders = referenceHolders;
    }

    @Override
    public List<ContainerData> getIntArrayReferenceHolders() {
        return Lists.newArrayList(referenceHolders);
    }
}
