package com.hrznstudio.titanium.client.gui;

import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.container.ContainerTileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerTileBase((TileBase) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiContainerTile((ContainerTileBase) getServerGuiElement(ID, player, world, x, y, z));
    }
}
