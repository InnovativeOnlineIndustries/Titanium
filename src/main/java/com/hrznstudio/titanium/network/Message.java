/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.Serializable;
import java.lang.reflect.Field;

public abstract class Message implements Serializable {

    protected abstract void handleMessage(NetworkEvent.Context context);

    public final void fromBytes(PacketBuffer buf) {
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

    public final void toBytes(PacketBuffer buf) {
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