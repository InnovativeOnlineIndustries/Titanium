/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.IEnumValues;
import com.hrznstudio.titanium.api.redstone.IRedstoneAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class RedstoneManager<T extends IEnumValues<T> & IRedstoneAction> implements INBTSerializable<CompoundNBT> {

    private T action;
    private boolean lastRedstoneState;
    private boolean shouldWork;

    public RedstoneManager(T defaultValue, boolean lastRedstoneState) {
        this.action = defaultValue;
        this.lastRedstoneState = lastRedstoneState;
        this.shouldWork = true;
    }

    public T getAction() {
        return action;
    }

    public void setAction(T action) {
        this.action = action;
    }

    public boolean getLastRedstoneState() {
        return lastRedstoneState;
    }

    public void setLastRedstoneState(boolean lastRedstoneState) {
        if (!this.lastRedstoneState && lastRedstoneState) {
            this.shouldWork = true;
        }
        this.lastRedstoneState = lastRedstoneState;
    }

    public boolean shouldWork() {
        return !action.startsOnChange() || shouldWork;
    }

    public void finish() {
        this.shouldWork = false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT value = new CompoundNBT();
        value.putString("Name", action.getName());
        value.putBoolean("LastState", lastRedstoneState);
        return value;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.action = this.action.getValue(nbt.getString("Name"));
        this.lastRedstoneState = nbt.getBoolean("LastState");
    }

}
