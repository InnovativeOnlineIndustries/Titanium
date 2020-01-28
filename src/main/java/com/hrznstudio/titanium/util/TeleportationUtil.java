/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

import java.util.LinkedList;

public class TeleportationUtil {


    public static Entity teleportEntity(Entity entity, DimensionType dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        if (entity == null || entity.world.isRemote) {
            return entity;
        }

        MinecraftServer server = entity.getServer();
        DimensionType sourceDim = entity.world.getDimension().getType();

        if (!entity.isBeingRidden() && !entity.isPassenger()) {
            return handleEntityTeleport(entity, server, sourceDim, dimension, xCoord, yCoord, zCoord, yaw, pitch);
        }

        Entity rootEntity = entity.getLowestRidingEntity();
        PassengerHelper passengerHelper = new PassengerHelper(rootEntity);
        PassengerHelper rider = passengerHelper.getPassenger(entity);
        if (rider == null) {
            return entity;
        }
        passengerHelper.teleport(server, sourceDim, dimension, xCoord, yCoord, zCoord, yaw, pitch);
        passengerHelper.remountRiders();
        passengerHelper.updateClients();

        return rider.entity;
    }

    /**
     * Convenience method that does not require pitch and yaw.
     */
    public static Entity teleportEntity(Entity entity, DimensionType dimension, double xCoord, double yCoord, double zCoord) {
        return teleportEntity(entity, dimension, xCoord, yCoord, zCoord, entity.rotationYaw, entity.rotationPitch);
    }

    /**
     * This is the base teleport method that figures out how to handle the teleport and makes it happen!
     */
    private static Entity handleEntityTeleport(Entity entity, MinecraftServer server, DimensionType sourceDim, DimensionType targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        if (entity == null || entity.world.isRemote) {
            return entity;
        }

        boolean interDimensional = sourceDim != targetDim;

        if (interDimensional && !ForgeHooks.onTravelToDimension(entity, targetDim)) {
            return entity;
        }

        if (interDimensional) {
            if (entity instanceof ServerPlayerEntity) {
                return teleportPlayerInterdimentional((ServerPlayerEntity) entity, server, sourceDim, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            } else {
                return teleportEntityInterdimentional(entity, server, sourceDim, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            }
        } else {
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                player.connection.setPlayerLocation(xCoord, yCoord, zCoord, yaw, pitch);
                player.setRotationYawHead(yaw);
            } else {
                entity.setLocationAndAngles(xCoord, yCoord, zCoord, yaw, pitch);
                entity.setRotationYawHead(yaw);
            }
        }

        return entity;
    }

    private static Entity teleportEntityInterdimentional(Entity entity, MinecraftServer server, DimensionType sourceDim, DimensionType targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        if (!entity.isAlive()) {
            return null;
        }

        ServerWorld sourceWorld = server.getWorld(sourceDim);
        ServerWorld targetWorld = server.getWorld(targetDim);

        //Set the entity dead before calling changeDimension. Still need to call changeDimension for things like minecarts which will drop their contents otherwise.
        if (entity.isAlive() && entity instanceof MinecartEntity) {
            entity.removed = true;
            entity.changeDimension(targetDim);
            entity.removed = false;
        }

        entity.dimension = targetDim;

        sourceWorld.removeEntity(entity);
        entity.removed = false;
        entity.setLocationAndAngles(xCoord, yCoord, zCoord, yaw, pitch);
        sourceWorld.updateEntity(entity);

        Entity newEntity = entity.getType().create(targetWorld);
        if (newEntity != null) {
            newEntity.copyDataFromOld(entity);
            newEntity.setLocationAndAngles(xCoord, yCoord, zCoord, yaw, pitch);
            boolean flag = newEntity.forceSpawn;
            newEntity.forceSpawn = true;
            targetWorld.addEntity(newEntity);
            newEntity.forceSpawn = flag;
            targetWorld.updateEntity(newEntity);
        }

        entity.removed = true;
        sourceWorld.resetUpdateEntityTick();
        targetWorld.resetUpdateEntityTick();

        return newEntity;
    }

    /**
     * This is the black magic responsible for teleporting players between dimensions!
     */
    private static PlayerEntity teleportPlayerInterdimentional(ServerPlayerEntity player, MinecraftServer server, DimensionType sourceDim, DimensionType destination, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        ServerWorld sourceWorld = server.getWorld(sourceDim);
        ServerWorld destinationWorld = server.getWorld(destination);
        WorldInfo sourceInfo = sourceWorld.getWorldInfo();
        PlayerList playerList = server.getPlayerList();

        player.dimension = destination;
        player.connection.sendPacket(new SRespawnPacket(destination, 0, sourceInfo.getGenerator(), player.interactionManager.getGameType()));
        player.connection.sendPacket(new SServerDifficultyPacket(sourceInfo.getDifficulty(), sourceInfo.isDifficultyLocked()));
        playerList.updatePermissionLevel(player);
        sourceWorld.removeEntity(player, true);
        player.revive();
        player.setLocationAndAngles(xCoord, yCoord, zCoord, yaw, pitch);
        player.setWorld(destinationWorld);
        destinationWorld.func_217447_b(player);
        player.connection.setPlayerLocation(xCoord, yCoord, zCoord, yaw, pitch);
        player.interactionManager.setWorld(destinationWorld);
        player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
        playerList.sendWorldInfo(player, destinationWorld);
        playerList.sendInventory(player);
        player.getActivePotionEffects().forEach(effectInstance -> player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectInstance)));
        player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
        BasicEventHooks.firePlayerChangedDimensionEvent(player, sourceDim, destination);
        return player;
    }

    public static Entity getHighestRidingEntity(Entity mount) {
        Entity entity;

        for (entity = mount; entity.getPassengers().size() > 0; entity = entity.getPassengers().get(0)) {
            ;
        }

        return entity;
    }

    private static class PassengerHelper {
        public Entity entity;
        public LinkedList<PassengerHelper> passengers = new LinkedList<>();
        public double offsetX, offsetY, offsetZ;

        /**
         * Creates a new passenger helper for the given entity and recursively adds all of the entities passengers.
         *
         * @param entity The root entity. If you have multiple stacked entities this would be the one at the bottom of the stack.
         */
        public PassengerHelper(Entity entity) {
            this.entity = entity;
            if (entity.isPassenger()) {
                offsetX = entity.getPosition().getX() - entity.getRidingEntity().getPosition().getX();
                offsetY = entity.getPosition().getY() - entity.getRidingEntity().getPosition().getY();
                offsetZ = entity.getPosition().getZ() - entity.getRidingEntity().getPosition().getZ();
            }
            for (Entity passenger : entity.getPassengers()) {
                passengers.add(new PassengerHelper(passenger));
            }
        }

        /**
         * Recursively teleports the entity and all of its passengers after dismounting them.
         *
         * @param server    The minecraft server.
         * @param sourceDim The source dimension.
         * @param targetDim The target dimension.
         * @param xCoord    The target x position.
         * @param yCoord    The target y position.
         * @param zCoord    The target z position.
         * @param yaw       The target yaw.
         * @param pitch     The target pitch.
         */
        public void teleport(MinecraftServer server, DimensionType sourceDim, DimensionType targetDim, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
            entity.removePassengers();
            entity = handleEntityTeleport(entity, server, sourceDim, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            for (PassengerHelper passenger : passengers) {
                passenger.teleport(server, sourceDim, targetDim, xCoord, yCoord, zCoord, yaw, pitch);
            }
        }

        /**
         * Recursively remounts all of this entities riders and offsets their position relative to their position before teleporting.
         */
        public void remountRiders() {
            if (entity.isPassenger()) {
                entity.setLocationAndAngles(entity.getPosition().getX() + offsetX, entity.getPosition().getY() + offsetY, entity.getPosition().getZ() + offsetZ, entity.rotationYaw, entity.rotationPitch);
            }
            for (PassengerHelper passenger : passengers) {
                passenger.entity.startRiding(entity, true);
                passenger.remountRiders();
            }
        }

        /**
         * This method sends update packets to any players that were teleported with the entity stack.
         */
        public void updateClients() {
            if (entity instanceof ServerPlayerEntity) {
                updateClient((ServerPlayerEntity) entity);
            }
            for (PassengerHelper passenger : passengers) {
                passenger.updateClients();
            }
        }

        /**
         * This is the method that is responsible for actually sending the update to each client.
         *
         * @param playerMP The Player.
         */
        private void updateClient(ServerPlayerEntity playerMP) {
            if (entity.isBeingRidden()) {
                playerMP.connection.sendPacket(new SSetPassengersPacket(entity));
            }
            for (PassengerHelper passenger : passengers) {
                passenger.updateClients();
            }
        }

        /**
         * This method returns the helper for a specific entity in the stack.
         *
         * @param passenger The passenger you are looking for.
         * @return The passenger helper for the specified passenger.
         */
        public PassengerHelper getPassenger(Entity passenger) {
            if (this.entity == passenger) {
                return this;
            }

            for (PassengerHelper rider : passengers) {
                PassengerHelper re = rider.getPassenger(passenger);
                if (re != null) {
                    return re;
                }
            }

            return null;
        }
    }
}
