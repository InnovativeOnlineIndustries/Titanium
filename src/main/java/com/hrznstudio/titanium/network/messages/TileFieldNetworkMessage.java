/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileFieldNetworkMessage extends Message {

    private BlockPos pos;
    private CompoundTag data;

    public TileFieldNetworkMessage(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    public TileFieldNetworkMessage() {
    }

    @Override
    protected void handleServerMessage(IPayloadContext context) {

    }

    @Override
    public void handleClientMessage(IPayloadContext context) {
        BlockEntity entity = context.player().level().getBlockEntity(pos);
        if (entity instanceof BasicTile){
            ((BasicTile<?>) entity).handleSyncObject(data);
        }
    }
}
