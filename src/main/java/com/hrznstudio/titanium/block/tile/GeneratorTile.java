/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;

public abstract class GeneratorTile<T extends GeneratorTile<T>> extends PoweredTile<T> {

    @Save
    private ProgressBarComponent<T> progressBar;

    public GeneratorTile(BasicTileBlock<T> basicTileBlock) {
        super(basicTileBlock);
        this.addProgressBar(progressBar = getProgressBar()
                .setComponentHarness(this.getSelf())
                .setCanIncrease(tileEntity -> !isSmart() || this.getEnergyCapacity() - this.getEnergyStorage().getEnergyStored() >= getEnergyProducedEveryTick())
                .setIncreaseType(false)
                .setOnStart(() -> {
                    progressBar.setMaxProgress(consumeFuel());
                    progressBar.setProgress(progressBar.getMaxProgress());
                    markForUpdate();
                })
                .setCanReset(tileEntity -> canStart() && progressBar.getProgress() == 0)
                .setOnTickWork(() -> this.getEnergyStorage().setEnergyStored(getEnergyProducedEveryTick() + this.getEnergyStorage().getEnergyStored()))
        );
    }

    /**
     * Consumes fuel successfully
     *
     * @return the amount of ticks the fuel will last for
     */
    public abstract int consumeFuel();

    /**
     * Gets if the generator can start
     *
     * @return True if the generator can start
     */
    public abstract boolean canStart();

    /**
     * @return The amount of energy produced every tick
     */
    public abstract int getEnergyProducedEveryTick();

    /**
     * Gets the progress bar used for the generator
     *
     * @return The progress bar
     */
    public abstract ProgressBarComponent<T> getProgressBar();

    /**
     * Gets how big the energy buffer on the generator is
     *
     * @return The amount of energy that can be stored
     */
    public abstract int getEnergyCapacity();

    /**
     * Gets how much energy can be extracted every tick
     *
     * @return The amount of energy that can be extracted
     */
    public abstract int getExtractingEnergy();

    /**
     * Defines is the generator wastes power when generating or not
     *
     * @return true is if it efficient, false if not
     */
    public boolean isSmart() {
        return true;
    }


    @Override
    public void tick() {
        super.tick();
        for (Direction facing : Direction.values()) {
            BlockPos checking = this.pos.offset(facing);
            TileEntity checkingTile = this.world.getTileEntity(checking);
            if (checkingTile != null) {
                checkingTile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(storage -> {
                    this.getEnergyStorage().extractEnergy(storage.receiveEnergy(this.getEnergyStorage().extractEnergy(this.getExtractingEnergy(), true), false), false);
                });
            }
        }
    }

    @Nonnull
    @Override
    protected EnergyStorageComponent<T> createEnergyStorage() {
        return new EnergyStorageComponent<>(getEnergyCapacity(),0, getExtractingEnergy(), 10, 20);
    }
}
