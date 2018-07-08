/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.container.ContainerTileBase;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    @Nonnull
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerTileBase<>(TileUtil.getTileEntity(world, new BlockPos(x, y, z), TileBase.class).orElseThrow(RuntimeException::new), player.inventory);
    }

    @Nonnull
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiContainerTile<>((ContainerTileBase<?>) getServerGuiElement(ID, player, world, x, y, z));
    }
}