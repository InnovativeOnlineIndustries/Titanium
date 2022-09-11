package com.hrznstudio.titanium.block_network.element;

import com.hrznstudio.titanium.api.block_network.NetworkElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface NetworkElementFactory {
    NetworkElement createFromNbt(Level level, CompoundTag tag);

}
