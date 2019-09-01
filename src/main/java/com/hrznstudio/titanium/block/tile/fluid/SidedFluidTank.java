/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.block.tile.sideness.SidedHandlerManager;
import com.hrznstudio.titanium.client.gui.addon.FacingHandlerGuiAddon;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class SidedFluidTank extends PosFluidTank implements IFacingHandler {

    private int color;
    private HashMap<FacingUtil.Sideness, FaceMode> facingModes;
    private int pos;

    public SidedFluidTank(String name, int amount, int posX, int posY, int pos) {
        super(name, amount, posX, posY);
        this.color = DyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        this.pos = pos;
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

    public SidedFluidTank setColor(DyeColor color) {
        this.color = color.getFireworkColor();
        return this;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getPosX(), this.getPosY(), 18 - 1, 46 - 1);
    }

    @Override
    public boolean work(World world, BlockPos pos, Direction blockFacing, int workAmount) {
        //TODO Implement when fluids work because idk if working so I wont bother
        return false;
    }

    @Override
    public java.util.List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = super.getGuiAddons();
        addons.add(() -> new FacingHandlerGuiAddon(SidedHandlerManager.ofRight(8, 84, pos, AssetTypes.BUTTON_SIDENESS_MANAGER, 4), this));
        return addons;
    }

    @Override
    public FluidTank readFromNBT(CompoundNBT nbt) {
        if (nbt.contains("FacingModes")) {
            CompoundNBT compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(FacingUtil.Sideness.valueOf(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
        return super.readFromNBT(nbt);
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT comp) {
        CompoundNBT nbt = super.writeToNBT(comp);
        CompoundNBT compound = new CompoundNBT();
        for (FacingUtil.Sideness facing : facingModes.keySet()) {
            compound.putString(facing.name(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

}
