package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.IEnumValues;
import com.hrznstudio.titanium.api.redstone.IRedstoneAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class RedstoneManager<T extends IEnumValues<T> & IRedstoneAction> implements INBTSerializable<CompoundNBT> {

    private T action;

    public RedstoneManager(T defaultValue) {
        this.action = defaultValue;
    }

    public T getAction() {
        return action;
    }

    public void setAction(T action) {
        this.action = action;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT value = new CompoundNBT();
        value.putString("Name", action.func_176610_l());
        return value;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.action = this.action.getValue(nbt.getString("Name"));
    }

}
