/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class TileFieldNetworkMessage extends Message {

    private BlockPos pos;
    private CompoundNBT data;

    public TileFieldNetworkMessage(BlockPos pos, CompoundNBT data) {
        this.pos = pos;
        this.data = data;
    }

    public TileFieldNetworkMessage() {
    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            TileEntity entity = Minecraft.getInstance().player.getEntityWorld().getTileEntity(pos);
            if (entity instanceof BasicTile){
                ((BasicTile<?>) entity).handleSyncObject(data);
            }
        });
    }
}
