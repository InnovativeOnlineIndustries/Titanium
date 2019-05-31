/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

import java.awt.*;
import java.util.HashMap;

public class SidedFluidTank extends PosFluidTank implements IFacingHandler {

    private int color;
    private HashMap<FacingUtil.Sideness, FaceMode> facingModes;

    public SidedFluidTank(int amount, int posX, int posY, String name) {
        super(amount, posX, posY, name);
        this.color = EnumDyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        for (FacingUtil.Sideness facing : FacingUtil.Sideness.values()) {
            this.facingModes.put(facing, FaceMode.ENABLED);
        }
    }

    @Override
    public HashMap<FacingUtil.Sideness, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public SidedFluidTank setColor(int color) {
        this.color = color;
        return this;
    }

    public SidedFluidTank setColor(EnumDyeColor color) {
        this.color = color.getFireworkColor();
        return this;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getPosX(), this.getPosY(), 18 - 1, 46 - 1);
    }

    @Override
    public boolean work(World world, BlockPos pos, EnumFacing blockFacing, int workAmount) {
        //TODO Implement when fluids work because idk if working so I wont bother
        return false;
    }


    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        if (nbt.contains("FacingModes")) {
            NBTTagCompound compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(FacingUtil.Sideness.valueOf(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
        return super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound comp) {
        NBTTagCompound nbt = super.writeToNBT(comp);
        NBTTagCompound compound = new NBTTagCompound();
        for (FacingUtil.Sideness facing : facingModes.keySet()) {
            compound.putString(facing.name(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

}
