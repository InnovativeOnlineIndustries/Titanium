/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;


import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockPosNBTHandler implements INBTHandler<BlockPos> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return BlockPos.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nonnull BlockPos object) {
        compound.putLong(name, object.asLong());
        return false;
    }

    @Override
    public BlockPos readFromNBT(@Nonnull CompoundTag compound, @Nonnull String name, @Nullable BlockPos current) {
        return compound.contains(name) ? BlockPos.of(compound.getLong(name)) : current;
    }
}
