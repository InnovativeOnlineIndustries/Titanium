/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.block.tile.progress;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.client.gui.addon.ProgressBarGuiAddon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PosProgressBar implements INBTSerializable<NBTTagCompound>, IGuiAddonProvider {

    private int posX;
    private int posY;
    private int progress;
    private int maxProgress;
    private int progressIncrease;
    private Predicate<TileEntity> canIncrease;
    private int tickingTime;
    private Runnable onFinishWork;
    private Runnable onTickWork;
    private TileBase tileBase;

    public PosProgressBar(int posX, int posY, int maxProgress) {
        this.posX = posX;
        this.posY = posY;
        this.progress = 0;
        this.maxProgress = maxProgress;
        this.progressIncrease = 1;
        this.canIncrease = tileEntity -> false;
        this.tickingTime = 1;
        this.onFinishWork = () -> {
        };
        this.onTickWork = () -> {
        };
    }

    public PosProgressBar setOnFinishWork(Runnable runnable) {
        this.onFinishWork = runnable;
        return this;
    }

    public PosProgressBar setOnTickWork(Runnable runnable) {
        this.onTickWork = runnable;
        return this;
    }

    public PosProgressBar setTickingTime(int tickingTime) {
        this.tickingTime = tickingTime;
        return this;
    }

    public PosProgressBar setTile(TileBase tileBase) {
        this.tileBase = tileBase;
        return this;
    }

    public void increase() {
        if (tileBase != null && tileBase.getWorld().getTotalWorldTime() % tickingTime == 0) {
            this.progress += progressIncrease;
            tileBase.markForUpdate();
            this.onTickWork.run();
        }
        if (progress > maxProgress) {
            this.progress = 0;
            this.onFinishWork.run();
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("Tick", progress);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        progress = nbt.getInteger("Tick");
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Predicate<TileEntity> getCanIncrease() {
        return canIncrease;
    }

    public PosProgressBar setCanIncrease(Predicate<TileEntity> canIncrease) {
        this.canIncrease = canIncrease;
        return this;
    }

    public TileBase getTileBase() {
        return tileBase;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (tileBase != null) tileBase.markForUpdate();
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public int getProgressIncrease() {
        return progressIncrease;
    }

    public PosProgressBar setProgressIncrease(int progressIncrease) {
        this.progressIncrease = progressIncrease;
        return this;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        return Collections.singletonList(() -> new ProgressBarGuiAddon(posX, posY, this));
    }
}
