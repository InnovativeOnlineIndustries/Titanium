/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.google.common.collect.Sets;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public abstract class PoweredTile<T extends PoweredTile<T>> extends ActiveTile<T> {
    @Save
    private final EnergyStorageComponent<T> energyStorage;
    private final LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.of(this::getEnergyStorage);

    public PoweredTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(base, blockEntityType, pos, state);
        this.energyStorage = this.createEnergyStorage();
        this.energyStorage.setComponentHarness(this.getSelf());
    }

    @Nonnull
    public EnergyStorageComponent<T> getEnergyStorage() {
        return energyStorage;
    }

    @Nonnull
    protected EnergyStorageComponent<T> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000, 4, 10);
    }

    public Set<Direction> getValidEnergyFaces() {
        return Sets.newHashSet(Direction.values());
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> screenAddons = super.getScreenAddons();
        if (this.getEnergyStorage().isEnabled()) {
            screenAddons.addAll(this.getEnergyStorage().getScreenAddons());
        }
        return screenAddons;
    }

    @Override
    @Nonnull
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> containerAddons = super.getContainerAddons();
        if (this.getEnergyStorage().isEnabled()) {
            containerAddons.addAll(this.getEnergyStorage().getContainerAddons());
        }
        return containerAddons;
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyEnergyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    public void showEnergyStorage() {
        this.getEnergyStorage().enable();
    }

    public void hideEnergyStorage() {
        this.getEnergyStorage().disable();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyStorage.invalidate();
    }
}
