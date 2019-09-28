/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAssetType;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.client.gui.addon.TankGuiAddon;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PosFluidTank extends FluidTank implements IGuiAddonProvider {

    private final int posX;
    private final int posY;
    private String name;
    private TileEntity tile;
    private Type tankType;
    private Action tankAction;

    public PosFluidTank(String name, int amount, int posX, int posY) {
        super(amount);
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        this.tankType = Type.NORMAL;
        this.tankAction = Action.BOTH;
    }

    /**
     * Sets the tile to be automatically marked dirty when the contents change
     *
     * @param tile The tile where the tank is
     * @return itself
     */
    public PosFluidTank setTile(TileEntity tile) {
        this.tile = tile;
        return this;
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

    public Type getTankType() {
        return tankType;
    }

    public PosFluidTank setTankType(Type tankType) {
        this.tankType = tankType;
        return this;
    }

    public Action getTankAction() {
        return tankAction;
    }

    public PosFluidTank setTankAction(Action tankAction) {
        this.tankAction = tankAction;
        return this;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return getTankAction().canFill() ? super.fill(resource, action) : 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return getTankAction().canDrain() ? super.drain(resource, action) : FluidStack.EMPTY;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return getTankAction().canDrain() ? super.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        addons.add(() -> new TankGuiAddon(this));
        return addons;
    }

    public enum Type {
        NORMAL(AssetTypes.TANK_NORMAL),
        SMALL(AssetTypes.TANK_SMALL);

        private final IAssetType assetType;

        Type(IAssetType assetType) {
            this.assetType = assetType;
        }

        public IAssetType getAssetType() {
            return assetType;
        }
    }

    public enum Action {
        FILL(true, false),
        DRAIN(false, true),
        BOTH(true, true),
        NONE(false, false);

        private final boolean fill;
        private final boolean drain;

        Action(boolean fill, boolean drain) {
            this.fill = fill;
            this.drain = drain;
        }

        public boolean canFill() {
            return fill;
        }

        public boolean canDrain() {
            return drain;
        }
    }
}
