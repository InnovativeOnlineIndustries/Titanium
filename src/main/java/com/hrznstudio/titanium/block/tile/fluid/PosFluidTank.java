/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.block.tile.TileBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class PosFluidTank extends FluidTank implements IFluidTankProperties {

    private final int posX;
    private final int posY;
    private Predicate<FluidStack> fillPredicate;
    private Predicate<FluidStack> drainPredicate;
    private String name;

    public PosFluidTank(int amount, int posX, int posY, String name) {
        super(amount);
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        this.fillPredicate = fluidStack1 -> true;
        this.drainPredicate = fluidStack1 -> true;
    }

    public PosFluidTank setFillFilter(Predicate<FluidStack> filter) {
        this.fillPredicate = filter;
        return this;
    }

    public PosFluidTank setDrainFilter(Predicate<FluidStack> filter) {
        this.drainPredicate = filter;
        return this;
    }

    public PosFluidTank setTile(TileEntity tile) {
        this.tile = tile;
        return this;
    }

    @Nullable
    @Override
    public FluidStack getContents() {
        return getFluid();
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        return fluidStack != null && fillPredicate.test(fluidStack);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack) {
        return fluidStack != null && drainPredicate.test(fluidStack);
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        if (tile instanceof TileBase) {
            ((TileBase) tile).markForUpdate();
        } else {
            tile.markDirty();
        }
    }

    public String getName() {
        return name;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
