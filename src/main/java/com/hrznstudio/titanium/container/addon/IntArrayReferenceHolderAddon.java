/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntReferenceHolder;

import java.util.List;

public class IntArrayReferenceHolderAddon implements IContainerAddon {
    private final IIntArray[] referenceHolders;

    public IntArrayReferenceHolderAddon(IIntArray... referenceHolders) {
        this.referenceHolders = referenceHolders;
    }

    @Override
    public List<IIntArray> getIntArrayReferenceHolders() {
        return Lists.newArrayList(referenceHolders);
    }
}
