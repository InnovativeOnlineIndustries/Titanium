/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class TeleportationUtils {


    public static Entity teleportEntity(Entity entity, ResourceKey<Level> dimension, double xCoord, double yCoord, double zCoord, float yaw, float pitch) {
        return teleportEntityTo(entity, new Vector3f((float) xCoord, (float) yCoord, (float) zCoord), dimension, yaw, pitch);
    }

    public static Entity teleportEntityTo(Entity entity, Vector3f target, ResourceKey<Level> destinationDimension, float yaw, float pitch) {
        if (entity.level().dimension() == destinationDimension) {
            entity.setYRot(yaw);
            entity.setXRot(pitch);
            entity.teleportTo(target.x() + 0.5, target.y(), target.z() + 0.5);

            return entity;
        } else {
            ServerLevel newWorld = ((ServerLevel) entity.level()).getServer().getLevel(destinationDimension);
            if (newWorld != null) {
                Vec3 destination = new Vec3(target.x() + 0.5, target.y(), target.z() + 0.5);
                var transition = new TeleportTransition(
                    newWorld, destination, entity.getDeltaMovement(), yaw, pitch, TeleportTransition.DO_NOTHING);
                return entity.teleport(transition);
            }
        }
        return null;
    }

    private static void teleportPassenger(ServerLevel destWorld, Entity repositionedEntity, Entity passenger) {
        //Note: We grab the passengers here instead of in placeEntity as changeDimension starts by removing any passengers
        List<Entity> passengers = passenger.getPassengers();
        passenger.teleport(new TeleportTransition(
            destWorld, repositionedEntity.position(), passenger.getDeltaMovement(), passenger.getYRot(), passenger.getXRot(), et -> {
            //Force our passenger to start riding the new entity again
            et.startRiding(repositionedEntity, true, true);
            //Teleport "nested" passengers
            for (Entity pas : passengers) {
                teleportPassenger(destWorld, et, pas);
            }
        }));
    }


}
