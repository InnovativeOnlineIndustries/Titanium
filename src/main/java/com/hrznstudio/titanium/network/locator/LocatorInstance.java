/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator;

import com.hrznstudio.titanium.network.CompoundSerializableDataHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Optional;

public abstract class LocatorInstance {
    private final LocatorType type;

    protected LocatorInstance(@Nonnull LocatorType type) {
        this.type = type;
    }

    @Nonnull
    public LocatorType getType() {
        return type;
    }

    public abstract Optional<?> locale(Player playerEntity);

    public ContainerLevelAccess getWorldPosCallable(Level world) {
        return ContainerLevelAccess.NULL;
    }

    public final void fromBytes(FriendlyByteBuf buf) {
        try {
            Class<?> clazz = getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible()) f.setAccessible(true);
                Class<?> type = f.getType();
                if (CompoundSerializableDataHandler.acceptField(f, type))
                    CompoundSerializableDataHandler.readField(f, type, buf, this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at reading packet " + this, e);
        }
    }

    public final void toBytes(FriendlyByteBuf buf) {
        try {
            Class<?> clazz = getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible()) f.setAccessible(true);
                Class<?> type = f.getType();
                if (CompoundSerializableDataHandler.acceptField(f, type))
                    CompoundSerializableDataHandler.writeField(f, type, buf, this);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at writing packet " + this, e);
        }
    }
}
