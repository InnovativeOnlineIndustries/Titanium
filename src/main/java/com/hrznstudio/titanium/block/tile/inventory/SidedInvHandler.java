/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.inventory;

import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.awt.*;
import java.util.HashMap;

public class SidedInvHandler extends PosInvHandler implements IFacingHandler {

    private int color;
    private HashMap<EnumFacing, FaceMode> facingModes;

    public SidedInvHandler(String name, int xPos, int yPos, int size) {
        super(name, xPos, yPos, size);
        this.color = EnumDyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        for (EnumFacing value : EnumFacing.values()) {
            this.facingModes.put(value, FaceMode.ENABLED);
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

    public SidedInvHandler setColor(int color) {
        this.color = color;
        return this;
    }

    public SidedInvHandler setColor(EnumDyeColor color) {
        this.color = color.getFireworkColor();
        return this;
    }

    @Override
    public Rectangle getRectangle() {
        int renderingOffset = 1;
        return new Rectangle(this.getXPos() - renderingOffset - 3, this.getYPos() - renderingOffset - 3, 18 * this.getXSize() + renderingOffset * 2 + 3, 18 * this.getYSize() + renderingOffset * 2 + 3);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        NBTTagCompound compound = new NBTTagCompound();
        for (EnumFacing facing : facingModes.keySet()) {
            compound.putString(facing.getName(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("FacingModes")) {
            NBTTagCompound compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(EnumFacing.byName(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
    }

}
