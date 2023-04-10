/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.List;
import java.util.function.Function;

public class TeleportationUtils {


    public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        return teleportEntityTo(entity, new BlockPos(xCoord, yCoord, zCoord), dimension, yaw ,pitch);
    }

    public static Entity teleportEntityTo(Entity entity, BlockPos target, ResourceKey<Level> destinationDimension, float yaw, float pitch) {
        if (entity.getCommandSenderWorld().dimension() == destinationDimension) {
            entity.setYRot(yaw);
            entity.setXRot(pitch);
            entity.teleportTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);

            if (!entity.getPassengers().isEmpty()) {
                //Force re-apply any passengers so that players don't get "stuck" outside what they may be riding
                ((ServerChunkCache) entity.getCommandSenderWorld().getChunkSource()).broadcast(entity, new ClientboundSetPassengersPacket(entity));
            }
            return entity;
        } else {
            ServerLevel newWorld = ((ServerLevel) entity.getCommandSenderWorld()).getServer().getLevel(destinationDimension);
            if (newWorld != null) {
                Vec3 destination = new Vec3(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
                //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
                List<Entity> passengers = entity.getPassengers();
                return entity.changeDimension(newWorld, new ITeleporter() {
                    @Override
                    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
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
                    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
                        return new PortalInfo(destination, entity.getDeltaMovement(), yaw, pitch);
                    }
                });
            }
        }
        return null;
    }

    private static void teleportPassenger(ServerLevel destWorld, Entity repositionedEntity, Entity passenger) {
        //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
        List<Entity> passengers = passenger.getPassengers();
        passenger.changeDimension(destWorld, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
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
