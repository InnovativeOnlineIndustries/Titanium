/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SMoveVehiclePacket;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ITeleporter;

import java.util.List;
import java.util.function.Function;
import net.minecraftforge.entity.PartEntity;

public class TeleportationUtils {


    public static Entity teleportEntity(Entity entity, RegistryKey<World> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        return teleportEntityTo(entity, new BlockPos(xCoord, yCoord, zCoord), dimension, yaw ,pitch);
    }

    public static Entity teleportEntityTo(Entity entity, BlockPos target, RegistryKey<World> destinationDimension, float yaw, float pitch) {
        if (entity instanceof PartEntity) {
            //Don't allow teleporting sub parts of an entity (such as an ender dragon's wing)
            return entity;
        }
        if (entity.getEntityWorld().getDimensionKey() == destinationDimension) {
            entity.rotationYaw = yaw;
            entity.rotationPitch = pitch;
            entity.setPositionAndUpdate(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);

            if (!entity.getPassengers().isEmpty()) {
                //Force re-apply any passengers so that players don't get "stuck" outside what they may be riding
                ((ServerChunkProvider) entity.getEntityWorld().getChunkProvider()).sendToAllTracking(entity, new SSetPassengersPacket(entity));
                Entity controller = entity.getControllingPassenger();
                if (controller != entity && controller instanceof ServerPlayerEntity && !(controller instanceof FakePlayer)) {
                    ServerPlayerEntity player = (ServerPlayerEntity) controller;
                    if (player.connection != null) {
                        //Force sync the fact that the vehicle moved to the client that is controlling it
                        // so that it makes sure to use the correct positions when sending move packets
                        // back to the server instead of running into moved wrongly issues
                        player.connection.sendPacket(new SMoveVehiclePacket(entity));
                    }
                }
            }
            return entity;
        } else if (entity.isNonBoss()) {
            //If the entity can change dimensions, try finding the destination world to teleport it to
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
                                teleportPassenger(destWorld, destination, repositionedEntity, passenger);
                            }
                        }
                        return repositionedEntity;
                    }

                    @Override
                    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                        return new PortalInfo(destination, entity.getMotion(), yaw, pitch);
                    }

                    @Override
                    public boolean playTeleportSound(ServerPlayerEntity player, ServerWorld sourceWorld, ServerWorld destWorld) {
                        return false;
                    }
                });
            }
        }
        //Fall back to returning the source entity without it having teleported
        return entity;
    }

    private static void teleportPassenger(ServerWorld destWorld, Vector3d destination, Entity repositionedEntity, Entity passenger) {
        if (!passenger.isNonBoss()) {
            //If the passenger can't change dimensions just let it peacefully stay after dismounting rather than trying to teleport it
            return;
        }
        //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
        List<Entity> passengers = passenger.getPassengers();
        passenger.changeDimension(destWorld, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                boolean invulnerable = entity.isInvulnerable();
                //Make the entity invulnerable so that when we teleport it, it doesn't take damage
                // we revert this state to the previous state after teleporting
                entity.setInvulnerable(true);
                Entity repositionedPassenger = repositionEntity.apply(false);
                if (repositionedPassenger != null) {
                    //Force our passenger to start riding the new entity again
                    repositionedPassenger.startRiding(repositionedEntity, true);
                    //Teleport "nested" passengers
                    for (Entity passenger : passengers) {
                        teleportPassenger(destWorld, destination, repositionedPassenger, passenger);
                    }
                    repositionedPassenger.setInvulnerable(invulnerable);
                }
                entity.setInvulnerable(invulnerable);
                return repositionedPassenger;
            }

            @Override
            public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
                //This is needed to ensure the passenger starts getting tracked after teleporting
                return new PortalInfo(destination, entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
            }

            @Override
            public boolean playTeleportSound(ServerPlayerEntity player, ServerWorld sourceWorld, ServerWorld destWorld) {
                return false;
            }
        });
    }


}
