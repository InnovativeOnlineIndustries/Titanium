/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock;

import com.hrznstudio.titanium.api.multiblock.IMultiblock;
import com.hrznstudio.titanium.event.custom.MultiblockFormationEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class ShapedMultiblockHandler {

    public static MultiblockFormationEvent multiblockFormationEventPre(PlayerEntity playerEntity, IMultiblock multiblock, BlockPos clickedState, ItemStack formationTool) {
        MultiblockFormationEvent event = new MultiblockFormationEvent.Pre(playerEntity, multiblock, clickedState, formationTool);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static MultiblockFormationEvent multiblockFormationEventPost(PlayerEntity playerEntity, IMultiblock multiblock, BlockPos controllerState, ItemStack formationTool) {
        MultiblockFormationEvent event = new MultiblockFormationEvent.Post(playerEntity, multiblock, controllerState, formationTool);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
}
