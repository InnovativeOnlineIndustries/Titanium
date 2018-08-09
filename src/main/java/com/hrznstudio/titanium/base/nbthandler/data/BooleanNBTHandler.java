package com.hrznstudio.titanium.base.nbthandler.data;

import com.hrznstudio.titanium.base.nbthandler.INBTHandler;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class BooleanNBTHandler implements INBTHandler<Boolean> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return boolean.class.isAssignableFrom(aClass) || Boolean.class.isAssignableFrom(aClass);
    }

    @Override
    public boolean storeToNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, @Nonnull Boolean object) {
        compound.setBoolean(name, object);
        return true;
    }

    @Override
    public Boolean readFromNBT(@Nonnull NBTTagCompound compound, @Nonnull String name, Boolean currentValue) {
        return compound.hasKey(name) ? compound.getBoolean(name) : null;
    }
}
