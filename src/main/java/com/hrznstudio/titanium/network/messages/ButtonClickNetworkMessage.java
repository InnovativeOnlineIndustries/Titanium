/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.messages;

import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.Message;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.util.CastingUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;


public class ButtonClickNetworkMessage extends Message {
    private LocatorInstance locatorInstance;
    private int id;
    private CompoundTag data;

    public ButtonClickNetworkMessage(LocatorInstance locatorInstance, int id, CompoundTag data) {
        this.locatorInstance = locatorInstance;
        this.id = id;
        this.data = data;
    }

    public ButtonClickNetworkMessage() {

    }

    @Override
    protected void handleServerMessage(IPayloadContext context) {
        Player player = context.player();
        // same reach as BasicAddonContainer#stillValid, so a client can't click buttons on far away (or unloaded) tiles
        boolean inReach = locatorInstance.getWorldPosCallable(player.level()).evaluate((level, pos) ->
            player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D, true);
        if (!inReach) return;
        Optional.of(player)
            .flatMap(locatorInstance::locale)
            .flatMap(CastingUtil.attemptCast(IButtonHandler.class))
            .ifPresent(iButtonHandler -> iButtonHandler.handleButtonMessage(id, player, data));
    }
}
