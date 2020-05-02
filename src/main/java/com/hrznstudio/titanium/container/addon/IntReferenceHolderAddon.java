/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.util.IntReferenceHolder;

import java.util.List;

public class IntReferenceHolderAddon implements IContainerAddon {
    private final IntReferenceHolder[] referenceHolders;

    public IntReferenceHolderAddon(IntReferenceHolder... referenceHolders) {
        this.referenceHolders = referenceHolders;
    }

    @Override
    public List<IntReferenceHolder> getIntReferenceHolders() {
        return Lists.newArrayList(referenceHolders);
    }
}
