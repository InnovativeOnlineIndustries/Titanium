/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;

public class NetworkHandler {

    private SimpleChannel network;
    private int i;

    public NetworkHandler(String modid) {
        i = 0;
        network = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(modid, "network"),
                () -> "1.0",
                s -> true,
                s -> true
        );
    }

    public SimpleChannel get() {
        return network;
    }

    public <REQ extends Message> void registerMessage(Class<REQ> message) {
        network.registerMessage(i++, message, Message::toBytes,
                buffer -> {
                    try {
                        REQ req = message.getConstructor().newInstance();
                        req.fromBytes(buffer);
                        return req;
                    } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                },
                (req, contextSupplier) -> {
                    NetworkEvent.Context context = contextSupplier.get();
                    req.handleMessage(context);
                    context.setPacketHandled(true);
                });
    }

    public void sendToNearby(World world, BlockPos pos, int distance, Message message) {
        world.getEntitiesWithinAABB(ServerPlayerEntity.class, new AxisAlignedBB(pos).grow(distance)).forEach(playerEntity -> {
            network.sendTo(message, playerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        });
    }
}