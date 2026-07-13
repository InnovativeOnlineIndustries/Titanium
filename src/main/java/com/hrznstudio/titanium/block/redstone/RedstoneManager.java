/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.redstone;

import com.hrznstudio.titanium.api.IEnumValues;
import com.hrznstudio.titanium.api.redstone.IRedstoneAction;
import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class RedstoneManager<T extends IEnumValues<T> & IRedstoneAction> implements INBTSerializable<CompoundTag> {

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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag value = new CompoundTag();
        value.putString("Name", action.getName());
        value.putBoolean("LastState", lastRedstoneState);
        value.putBoolean("ShouldWork", shouldWork);
        return value;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.action = this.action.getValue(nbt.getStringOr("Name", ""));
        this.lastRedstoneState = nbt.getBooleanOr("LastState", false);
        this.shouldWork = nbt.getBooleanOr("ShouldWork", false);
    }

}
