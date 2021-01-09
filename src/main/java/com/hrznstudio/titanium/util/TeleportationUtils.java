/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.List;
import java.util.function.Function;

public class TeleportationUtils {


    public static Entity teleportEntity(Entity entity, RegistryKey<World> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        if (entity == null || entity.world.isRemote) {
            return entity;
        }

        MinecraftServer server = entity.getServer();
        List<Entity> passengers = entity.getPassengers();
        return entity.changeDimension(server.getWorld(dimension), new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yawE, Function<Boolean, Entity> repositionEntity) {
                Entity repositionedEntity = repositionEntity.apply(false);
                if (repositionedEntity != null){
                    repositionedEntity.setPositionAndRotation(xCoord, yCoord, zCoord, yaw, pitch);
                    repositionedEntity.setPositionAndUpdate(xCoord, yCoord, zCoord);
                    for (Entity passenger : passengers) {
                        teleportPassenger(destWorld, repositionedEntity, passenger);
                    }
                }
                return repositionedEntity;
            }
        });
    }

    private static void teleportPassenger(ServerWorld destWorld, Entity repositionedEntity, Entity passenger) {
        //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
        List<Entity> passengers = passenger.getPassengers();
        passenger.changeDimension(destWorld, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                Entity repositionedPassenger = repositionEntity.apply(false);
                if (repositionedPassenger != null) {
                    //Force our passenger to start riding the new entity again
                    repositionedPassenger.startRiding(repositionedEntity, true);
                    //Teleport "nested" passengers
                    for (Entity passenger : passengers) {
                        teleportPassenger(destWorld, repositionedPassenger, passenger);
                    }
                }
                return repositionedPassenger;
            }
        });
    }


}
