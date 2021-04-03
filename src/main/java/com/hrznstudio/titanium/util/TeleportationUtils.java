/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.List;
import java.util.function.Function;

public class TeleportationUtils {


    public static Entity teleportEntity(Entity entity, RegistryKey<World> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        return teleportEntityTo(entity, new BlockPos(xCoord, yCoord, zCoord), dimension, yaw ,pitch);
    }

    public static Entity teleportEntityTo(Entity entity, BlockPos target, RegistryKey<World> destinationDimension, float yaw, float pitch) {
        if (entity.getEntityWorld().getDimensionKey() == destinationDimension) {
            entity.rotationYaw = yaw;
            entity.rotationPitch = pitch;
            entity.setPositionAndUpdate(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);

            if (!entity.getPassengers().isEmpty()) {
                //Force re-apply any passengers so that players don't get "stuck" outside what they may be riding
                ((ServerChunkProvider) entity.getEntityWorld().getChunkProvider()).sendToAllTracking(entity, new SSetPassengersPacket(entity));
            }
            return entity;
        } else {
            ServerWorld newWorld = ((ServerWorld) entity.getEntityWorld()).getServer().getWorld(destinationDimension);
            if (newWorld != null) {
                Vector3d destination = new Vector3d(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
                List<Entity> passengers = entity.getPassengers();
                return entity.changeDimension(newWorld, new ITeleporter() {
                    @Override
                    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                        Entity repositionedEntity = repositionEntity.apply(false);
                        if (repositionedEntity != null) {
                            //Teleport all passengers to the other dimension and then make them start riding the entity again
                            for (Entity passenger : passengers) {
                                teleportPassenger(destWorld, repositionedEntity, passenger);
                            }
                        }
                        return repositionedEntity;
                    }

                    @Override
                    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                        return new PortalInfo(destination, entity.getMotion(), yaw, pitch);
                    }
                });
            }
        }
        return null;
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
