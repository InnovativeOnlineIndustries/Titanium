/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.energy;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.addon.IntReferenceHolderAddon;
import com.hrznstudio.titanium.container.referenceholder.FunctionReferenceHolder;
import com.hrznstudio.titanium.nbthandler.INBTSerializable;
import com.hrznstudio.titanium.util.ValueIOSerialization;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class EnergyStorageComponent<T extends IComponentHarness> extends SimpleEnergyHandler implements
    IScreenAddonProvider, IContainerAddonProvider, INBTSerializable<CompoundTag> {

    private final int xPos;
    private final int yPos;

    protected T componentHarness;

    public EnergyStorageComponent(int maxCapacity, int xPos, int yPos) {
        this(maxCapacity, maxCapacity, xPos, yPos);
    }

    public EnergyStorageComponent(int maxCapacity, int maxIO, int xPos, int yPos) {
        this(maxCapacity, maxIO, maxIO, xPos, yPos);
    }

    public EnergyStorageComponent(int maxCapacity, int maxReceive, int maxExtract, int xPos, int yPos) {
        super(maxCapacity, maxReceive, maxExtract);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getEnergyStored() {
        return getAmountAsInt();
    }

    public void setEnergyStored(int energy) {
        set(Math.min(Math.max(energy, 0), getCapacityAsInt()));
    }

    public int getMaxEnergyStored() {
        return getCapacityAsInt();
    }

    @Override
    protected void onEnergyChanged(int previousAmount) {
        update();
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Lists.newArrayList(
            () -> new EnergyBarScreenAddon(xPos, yPos, this)
        );
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Lists.newArrayList(
            () -> new IntReferenceHolderAddon(new FunctionReferenceHolder(this::setEnergyStored, this::getEnergyStored))
        );
    }

    public void setComponentHarness(T componentHarness) {
        this.componentHarness = componentHarness;
    }

    private void update() {
        if (this.componentHarness != null) {
            this.componentHarness.markComponentForUpdate(true);
        }
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return ValueIOSerialization.save(provider, this);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        ValueIOSerialization.load(provider, nbt, this);
    }
}

