package com.hrznstudio.titanium.nbthandler.data;

import com.hrznstudio.titanium.api.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class EnumNBTHandler<E extends Enum<E>> implements INBTHandler<E> {
    protected abstract int getIdFrom(E obj);

    protected abstract E getFromId(int id);

    @Override
    public final boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull E object) {
        compound.putInt(name, getIdFrom(object));
        return true;
    }

    @Override
    public final E readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nullable E currentValue) {
        return compound.contains(name) ? getFromId(compound.getInt(name)) : currentValue;
    }
}