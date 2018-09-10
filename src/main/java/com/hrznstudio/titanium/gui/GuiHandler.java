/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.gui;

import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.util.TileUtil;
import com.hrznstudio.titanium.container.ContainerTileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nonnull;

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