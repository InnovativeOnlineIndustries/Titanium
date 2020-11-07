/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import java.util.List;

public class EnergyStorageComponent<T extends IComponentHarness> extends EnergyStorage implements INBTSerializable<CompoundNBT>,
    IScreenAddonProvider, IContainerAddonProvider {

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

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int amount = super.receiveEnergy(maxReceive, simulate);
        if (!simulate && amount > 0) {
            this.update();
        }
        return amount;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int amount = super.extractEnergy(maxExtract, simulate);
        if (!simulate && amount > 0) {
            this.update();
        }
        return amount;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("energy", this.energy);
        return nbt;
    }

    public void setEnergyStored(int energy) {
        if (energy > this.getMaxEnergyStored()) {
            this.energy = this.getMaxEnergyStored();
        } else {
            this.energy = Math.max(energy, 0);
        }
        this.update();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }

    @Override
    @Nonnull
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
}

