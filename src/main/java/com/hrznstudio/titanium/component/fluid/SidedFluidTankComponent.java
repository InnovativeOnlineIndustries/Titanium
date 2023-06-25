/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.fluid;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.client.screen.addon.FacingHandlerScreenAddon;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.SidedComponentManager;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class SidedFluidTankComponent<T extends IComponentHarness> extends FluidTankComponent<T> implements IFacingComponent, IScreenAddonProvider {

    private int color;
    private int facingHandlerX = 8;
    private int facingHandlerY = 84;
    private HashMap<FacingUtil.Sideness, FaceMode> facingModes;
    private int pos;
    private boolean hasFacingAddon;
    private FaceMode[] validFaceModes;

    public SidedFluidTankComponent(String name, int amount, int posX, int posY, int pos) {
        super(name, amount, posX, posY);
        this.color = DyeColor.WHITE.getFireworkColor();
        this.facingModes = new HashMap<>();
        this.pos = pos;
        for (FacingUtil.Sideness facing : FacingUtil.Sideness.values()) {
            this.facingModes.put(facing, FaceMode.ENABLED);
        }
        this.hasFacingAddon = true;
        this.validFaceModes = FaceMode.values();
    }

    public SidedFluidTankComponent<T> disableFacingAddon() {
        this.hasFacingAddon = false;
        return this;
    }

    @Override
    public HashMap<FacingUtil.Sideness, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public SidedFluidTankComponent<T> setColor(int color) {
        this.color = color;
        return this;
    }

    public SidedFluidTankComponent<T> setColor(DyeColor color) {
        this.color = color.getFireworkColor();
        return this;
    }

    @Override
    public Rectangle getRectangle(IAsset asset) {
        return new Rectangle(this.getPosX() - 2, this.getPosY() - 2, (int) asset.getArea().getWidth() + 3, (int) asset.getArea().getHeight() + 3);
    }

    @Override
    public int getFacingHandlerX() {
        return this.facingHandlerX;
    }

    @Override
    public int getFacingHandlerY() {
        return this.facingHandlerY;
    }


    @Override
    public boolean work(Level world, BlockPos pos, Direction blockFacing, int workAmount) {
        for (FacingUtil.Sideness sideness : facingModes.keySet()) {
            if (facingModes.get(sideness).equals(FaceMode.PUSH)) {
                Direction real = FacingUtil.getFacingFromSide(blockFacing, sideness);
                BlockEntity entity = world.getBlockEntity(pos.relative(real));
                if (entity != null) {
                    boolean hasWorked = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, real.getOpposite())
                        .map(iFluidHandler -> transfer(this, iFluidHandler, workAmount))
                        .orElse(false);
                    if (hasWorked) {
                        return true;
                    }
                }
            }
        }
        for (FacingUtil.Sideness sideness : facingModes.keySet()) {
            if (facingModes.get(sideness).equals(FaceMode.PULL)) {
                Direction real = FacingUtil.getFacingFromSide(blockFacing, sideness);
                BlockEntity entity = world.getBlockEntity(pos.relative(real));
                if (entity != null) {
                    boolean hasWorked = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, real.getOpposite())
                        .map(iFluidHandler -> transfer(iFluidHandler, this, workAmount))
                        .orElse(false);
                    if (hasWorked) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public SidedFluidTankComponent<T> setFacingHandlerPos(int x, int y) {
        this.facingHandlerX = x;
        this.facingHandlerY = y;
        return this;
    }


    @Override
    public FaceMode[] getValidFacingModes() {
        return validFaceModes;
    }

    public SidedFluidTankComponent<T> setValidFaceModes(FaceMode... validFaceModes){
        this.validFaceModes = validFaceModes;
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            this.facingModes.put(value, validFaceModes[0]);
        }
        return this;
    }

    private boolean transfer(IFluidHandler from, IFluidHandler to, int workAmount) {
        FluidStack stack = from.drain(workAmount * 100, FluidAction.SIMULATE);
        if (!stack.isEmpty()) {
            stack = from.drain(to.fill(stack, FluidAction.EXECUTE), FluidAction.EXECUTE);
            return !stack.isEmpty();
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = super.getScreenAddons();
        if (hasFacingAddon)
            addons.add(() -> new FacingHandlerScreenAddon(SidedComponentManager.ofRight(getFacingHandlerX(), getFacingHandlerY(), pos, AssetTypes.BUTTON_SIDENESS_MANAGER, 4), this, getTankType().getAssetType(), this.getComponentHarness() instanceof ActiveTile ? ((ActiveTile) this.getComponentHarness()).getFacingDirection() : Direction.NORTH));
        return addons;
    }

    @Override
    public FluidTank readFromNBT(CompoundTag nbt) {
        if (nbt.contains("FacingModes")) {
            CompoundTag compound = nbt.getCompound("FacingModes");
            for (String face : compound.getAllKeys()) {
                facingModes.put(FacingUtil.Sideness.valueOf(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
        return super.readFromNBT(nbt);
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag comp) {
        CompoundTag nbt = super.writeToNBT(comp);
        CompoundTag compound = new CompoundTag();
        for (FacingUtil.Sideness facing : facingModes.keySet()) {
            compound.putString(facing.name(), facingModes.get(facing).name());
        }
        nbt.put("FacingModes", compound);
        return nbt;
    }

}
