/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.block.tile.TileActive;
import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.util.TileUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class ButtonClickNetworkMessage extends Message {
    private BlockPos pos;
    private int id;
    private CompoundNBT data;

    public ButtonClickNetworkMessage(BlockPos pos, int id, CompoundNBT data) {
        this.pos = pos;
        this.id = id;
        this.data = data;
    }

    public ButtonClickNetworkMessage() {

    }

    @Override
    protected void handleMessage(NetworkEvent.Context context) {
        TileUtil.getTileEntity(context.getSender().world, pos, TileActive.class).ifPresent(tileActive -> {
            tileActive.handleButtonMessage(id, context.getSender(), data);
        });
    }
}
