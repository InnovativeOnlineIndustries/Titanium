/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.container.ContainerTileBase;
import com.hrznstudio.titanium.util.TileUtil;
import mcmultipart.MCMultiPart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class MCMPGuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public ContainerTileBase getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != -1) {
            IPartSlot slot = MCMultiPart.slotRegistry.getValue(ID);
            return MultipartHelper.getPartTile(world, new BlockPos(x, y, z), slot).map(IMultipartTile::getTileEntity).filter(TileBase.class::isInstance).map(TileBase.class::cast).map(tile -> new ContainerTileBase<>(tile, player.inventory)).orElse(null);
        }
        return new ContainerTileBase<>(TileUtil.getTileEntity(world, new BlockPos(x, y, z), TileBase.class).orElseThrow(RuntimeException::new), player.inventory);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ContainerTileBase<?> containerTileBase = getServerGuiElement(ID, player, world, x, y, z);
        if (containerTileBase != null)
            return new GuiContainerTile<>(containerTileBase);
        return null;
    }
}