/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;

import java.awt.*;
import java.util.HashMap;

public class SidedFluidTank extends PosFluidTank implements IFacingHandler {

    private int color;
    private HashMap<EnumFacing, FaceMode> facingModes;

    public SidedFluidTank(int amount, int posX, int posY, String name) {
        super(amount, posX, posY, name);
        this.color = EnumDyeColor.WHITE.func_196060_f();
        this.facingModes = new HashMap<>();
        for (EnumFacing facing : EnumFacing.values()) {
            this.facingModes.put(facing, FaceMode.ENABLED);
        }
    }

    @Override
    public HashMap<EnumFacing, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public SidedFluidTank setColor(EnumDyeColor color) {
        this.color = color.func_196060_f();
        return this;
    }

    public SidedFluidTank setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getPosX(), this.getPosY(), 18 - 1, 46 - 1);
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("FacingModes")) {
            NBTTagCompound compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(EnumFacing.byName(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
        return super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound comp) {
        NBTTagCompound nbt = super.writeToNBT(comp);
        NBTTagCompound compound = new NBTTagCompound();
        for (EnumFacing facing : facingModes.keySet()) {
            compound.setString(facing.getName(), facingModes.get(facing).name());
        }
        nbt.setTag("FacingModes", compound);
        return nbt;
    }

}
