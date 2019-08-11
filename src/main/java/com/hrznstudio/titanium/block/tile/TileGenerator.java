package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.client.gui.addon.EnergyBarGuiAddon;
import com.hrznstudio.titanium.energy.NBTEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class TileGenerator extends TilePowered {

    private int capacity;
    private int extractingEnergy;
    @Save
    private PosProgressBar progressBar;

    public TileGenerator(BlockTileBase blockTileBase, int capacity, int extractingEnergy) {
        super(blockTileBase);
        this.addGuiAddonFactory(() -> new EnergyBarGuiAddon(10, 20, getEnergyStorage()));
        this.capacity = capacity;
        this.extractingEnergy = extractingEnergy;
        this.addProgressBar(progressBar = getProgressBar()
                .setTile(this)
                .setCanIncrease(tileEntity -> true)
                .setOnStart(() -> {
                    progressBar.setMaxProgress(consumeFuel());
                    markForUpdate();
                })
                .setCanReset(tileEntity -> canStart())
                .setOnTickWork(() -> this.getEnergyStorage().extractEnergyForced(getEnergyProducedEveryTick()))
        );
    }

    @Override
    protected IFactory<NBTEnergyHandler> getEnergyHandlerFactory() {
        return () -> new NBTEnergyHandler(this, capacity, 0, extractingEnergy);
    }

    /**
     * Consumes fuel successfully
     *
     * @return the amount of ticks the fuel will last for
     */
    abstract int consumeFuel();

    /**
     * Gets if the generator can start
     *
     * @return True if the generator can start
     */
    abstract boolean canStart();

    /**
     * @return The amount of energy produced every tick
     */
    abstract int getEnergyProducedEveryTick();

    /**
     * Gets the progress bar used for the generator
     *
     * @return The progress bar
     */
    abstract PosProgressBar getProgressBar();


    @Override
    public void tick() {
        super.tick();
        for (Direction facing : Direction.values()) {
            BlockPos checking = this.pos.offset(facing);
            TileEntity checkingTile = this.world.getTileEntity(checking);
            if (checkingTile != null) {
                checkingTile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(storage -> {
                    int energy = storage.receiveEnergy(Math.min(this.getEnergyStorage().getEnergyStored(), extractingEnergy), false);
                    if (energy > 0) {
                        this.getEnergyStorage().extractEnergy(energy, false);
                        return;
                    }
                });
            }
        }

    }
}
