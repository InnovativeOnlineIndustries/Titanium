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
