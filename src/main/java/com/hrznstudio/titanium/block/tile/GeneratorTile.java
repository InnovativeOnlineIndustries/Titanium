/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.progress.IProgressing;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon;
import com.hrznstudio.titanium.energy.NBTEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class GeneratorTile<T extends BasicTileBlock<T, U>, U extends GeneratorTile<T, U>> extends PoweredTile<T, U> implements IProgressing {

    @Save
    private PosProgressBar<GeneratorTile<T, U>> progressBar;

    public GeneratorTile(T blockTileBase) {
        super(blockTileBase);
        this.addScreenAddonFactory(() -> new EnergyBarScreenAddon(10, 20, getEnergyStorage()));
        this.addProgressBar(progressBar = getProgressBar()
                .setProgressing(this)
                .setCanIncrease(tileEntity -> !isSmart() || this.getEnergyCapacity() - this.getEnergyStorage().getEnergyStored() >= getEnergyProducedEveryTick())
                .setIncreaseType(false)
                .setOnStart(() -> {
                    progressBar.setMaxProgress(consumeFuel());
                    progressBar.setProgress(progressBar.getMaxProgress());
                    markForUpdate();
                })
                .setCanReset(tileEntity -> canStart() && progressBar.getProgress() == 0)
                .setOnTickWork(() -> this.getEnergyStorage().receiveEnergyForced(getEnergyProducedEveryTick()))
        );
    }

    @Override
    protected IFactory<NBTEnergyHandler> getEnergyHandlerFactory() {
        return () -> new NBTEnergyHandler(this, getEnergyCapacity(), 0, getExtractingEnergy());
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
    public abstract PosProgressBar<GeneratorTile<T, U>> getProgressBar();

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
                    int energy = storage.receiveEnergy(Math.min(this.getEnergyStorage().getEnergyStored(), getExtractingEnergy()), false);
                    if (energy > 0) {
                        this.getEnergyStorage().extractEnergy(energy, false);
                        return;
                    }
                });
            }
        }
    }
}
