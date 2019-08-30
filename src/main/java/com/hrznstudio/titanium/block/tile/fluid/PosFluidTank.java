/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.block.tile.TileBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class PosFluidTank extends FluidTank {

    private final int posX;
    private final int posY;
    private String name;
    private TileEntity tile;

    public PosFluidTank(int amount, int posX, int posY, String name) {
        super(amount);
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        ;
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
}
