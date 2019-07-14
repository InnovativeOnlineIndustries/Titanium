/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;

import java.util.LinkedList;

public class TeleportationUtils {


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
        DimensionType dimensiontype = player.dimension;
        if (dimensiontype == DimensionType.THE_END && destination == DimensionType.OVERWORLD) {
            player.detach();
            player.getServerWorld().removePlayer(player);
            if (!player.queuedEndExit) {
                player.queuedEndExit = true;
            }
            return player;
        } else {
            ServerWorld serverworld = player.server.getWorld(dimensiontype);
            player.dimension = destination;
            ServerWorld serverworld1 = player.server.getWorld(destination);
            WorldInfo worldinfo = player.world.getWorldInfo();
            player.connection.sendPacket(new SRespawnPacket(destination, worldinfo.getGenerator(), player.interactionManager
                    .getGameType()));
            player.connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo
                    .isDifficultyLocked()));
            PlayerList playerlist = player.server.getPlayerList();
            playerlist.updatePermissionLevel(player);
            serverworld.removeEntity(player, true); //Forge: the player entity is moved to the new world, NOT cloned. So keep the data alive with no matching invalidate call.
            player.revive();
            double d0 = player.posX;
            double d1 = player.posY;
            double d2 = player.posZ;
            float f = player.rotationPitch;
            float f1 = player.rotationYaw;
            double d3 = 8.0D;
            float f2 = f1;
            serverworld.getProfiler().startSection("moving");
            double moveFactor = serverworld.getDimension()
                    .getMovementFactor() / serverworld1.getDimension()
                    .getMovementFactor();
            d0 *= moveFactor;
            d2 *= moveFactor;
            if (dimensiontype == DimensionType.OVERWORLD && destination == DimensionType.THE_NETHER) {
                player.enteredNetherPosition = new Vec3d(player.posX, player.posY, player.posZ);
            } else if (dimensiontype == DimensionType.OVERWORLD && destination == DimensionType.THE_END) {
                BlockPos blockpos = serverworld1.getSpawnCoordinate();
                d0 = (double) blockpos.getX();
                d1 = (double) blockpos.getY();
                d2 = (double) blockpos.getZ();
                f1 = 90.0F;
                f = 0.0F;
            }

            serverworld.getProfiler().endSection();

            player.setLocationAndAngles(xCoord, yCoord, zCoord, yaw, pitch);
            player.setMotion(Vec3d.ZERO);

            player.setWorld(serverworld1);
            serverworld1.func_217447_b(player);
            player.func_213846_b(serverworld);
            player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, f1, f);
            player.interactionManager.setWorld(serverworld1);
            player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
            playerlist.sendWorldInfo(player, serverworld1);
            playerlist.sendInventory(player);

            for (EffectInstance effectinstance : player.getActivePotionEffects()) {
                player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
            }

            player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerChangedDimensionEvent(player, dimensiontype, destination);
            return player;
        }
    }

    public static Entity getHighestRidingEntity(Entity mount) {
        Entity entity;

        for (entity = mount; entity.getPassengers().size() > 0; entity = entity.getPassengers().get(0)) ;

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
                offsetX = entity.posX - entity.getRidingEntity().posX;
                offsetY = entity.posY - entity.getRidingEntity().posY;
                offsetZ = entity.posZ - entity.getRidingEntity().posZ;
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
                entity.setLocationAndAngles(entity.posX + offsetX, entity.posY + offsetY, entity.posZ + offsetZ, entity.rotationYaw, entity.rotationPitch);
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
