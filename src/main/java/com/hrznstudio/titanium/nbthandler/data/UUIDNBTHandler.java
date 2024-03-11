/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class UUIDNBTHandler implements INBTHandler<UUID> {
    @Override
    public boolean isClassValid(Class<?> aClass) {
        return UUID.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nonnull UUID object) {
        compound.putUniqueId(name, object);
        return true;
    }

    @Override
    public UUID readFromNBT(@Nonnull CompoundNBT compound, @Nonnull String name, @Nullable UUID current) {
        return compound.contains(name) ? compound.getUniqueId(name) : current;
    }
}
