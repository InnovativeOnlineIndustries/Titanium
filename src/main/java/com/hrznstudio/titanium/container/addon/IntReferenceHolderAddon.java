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
