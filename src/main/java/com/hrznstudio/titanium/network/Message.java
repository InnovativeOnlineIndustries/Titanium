/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public abstract class Message implements Serializable {
    private static final HashMap<Class, Pair<Reader, Writer>> handlers = new HashMap<>();

    static {
        map(byte.class, PacketBuffer::readByte, (Writer<Byte>) PacketBuffer::writeByte);
        map(short.class, PacketBuffer::readShort, (Writer<Short>) PacketBuffer::writeShort);
        map(int.class, PacketBuffer::readVarInt, PacketBuffer::writeVarInt);
        map(long.class, PacketBuffer::readVarLong, PacketBuffer::writeVarLong);
        map(float.class, PacketBuffer::readFloat, PacketBuffer::writeFloat);
        map(double.class, PacketBuffer::readDouble, PacketBuffer::writeDouble);
        map(boolean.class, PacketBuffer::readBoolean, PacketBuffer::writeBoolean);
        map(char.class, PacketBuffer::readChar, (Writer<Character>) PacketBuffer::writeChar);

        map(Byte.class, PacketBuffer::readByte, (Writer<Byte>) PacketBuffer::writeByte);
        map(Short.class, PacketBuffer::readShort, (Writer<Short>) PacketBuffer::writeShort);
        map(Integer.class, PacketBuffer::readVarInt, PacketBuffer::writeVarInt);
        map(Long.class, PacketBuffer::readVarLong, PacketBuffer::writeVarLong);
        map(Float.class, PacketBuffer::readFloat, PacketBuffer::writeFloat);
        map(Double.class, PacketBuffer::readDouble, PacketBuffer::writeDouble);
        map(Boolean.class, PacketBuffer::readBoolean, PacketBuffer::writeBoolean);
        map(Character.class, PacketBuffer::readChar, (Writer<Character>) PacketBuffer::writeChar);

        map(byte[].class, PacketBuffer::readByteArray, PacketBuffer::writeByteArray);
        map(int[].class, PacketBuffer::readVarIntArray, PacketBuffer::writeVarIntArray);
        map(long[].class, Message::readLongArray, PacketBuffer::writeLongArray);

        map(String.class, Message::readString, PacketBuffer::writeString);
        map(NBTTagCompound.class, PacketBuffer::readCompoundTag, PacketBuffer::writeCompoundTag);
        map(ItemStack.class, PacketBuffer::readItemStack, PacketBuffer::writeItemStack);
        map(FluidStack.class, Message::readFluidStack, Message::writeFluidStack);
        map(BlockPos.class, PacketBuffer::readBlockPos, PacketBuffer::writeBlockPos);
        map(ITextComponent.class, PacketBuffer::readTextComponent, PacketBuffer::writeTextComponent);
        map(Date.class, PacketBuffer::readTime, PacketBuffer::writeTime);
        map(UUID.class, PacketBuffer::readUniqueId, PacketBuffer::writeUniqueId);
        map(SPacketUpdateTileEntity.class, Message::readUpdatePacket, Message::writeUpdatePacket);
    }

    private static long[] readLongArray(PacketBuffer buf) {
        return buf.readLongArray(new long[0]);
    }

    private static String readString(PacketBuffer buf) {
        return buf.readString(32767);
    }

    private static FluidStack readFluidStack(PacketBuffer buf) throws IOException {
        return FluidStack.loadFluidStackFromNBT(buf.readCompoundTag());
    }

    private static void writeFluidStack(PacketBuffer buf, FluidStack stack) {
        buf.writeCompoundTag(stack.writeToNBT(new NBTTagCompound()));
    }

    private static void writeUpdatePacket(PacketBuffer buf, SPacketUpdateTileEntity packet) {
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static SPacketUpdateTileEntity readUpdatePacket(PacketBuffer buf) {
        SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity();
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packet;
    }

    private static Pair<Reader, Writer> getHandler(Class<?> clazz) {
        Pair<Reader, Writer> pair = handlers.get(clazz);
        if (pair == null)
            throw new RuntimeException("No R/W handler for  " + clazz);
        return pair;
    }

    private static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) && handlers.containsKey(type);

    }

    public static <T> void map(Class<T> type, Reader<T> reader, Writer<T> writer) {
        handlers.put(type, Pair.of(reader, writer));
    }

    protected abstract void handleMessage(NetworkEvent.Context context);

    public final void fromBytes(PacketBuffer buf) {
        try {
            Class<?> clazz = getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible()) f.setAccessible(true);
                Class<?> type = f.getType();
                if (acceptField(f, type))
                    readField(f, type, buf);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at reading packet " + this, e);
        }
    }

    private void writeField(Field f, Class clazz, PacketBuffer buf) throws IllegalArgumentException, IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        handler.getRight().write(buf, f.get(this));
    }

    private void readField(Field f, Class clazz, PacketBuffer buf) throws IllegalArgumentException, IllegalAccessException, IOException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        f.set(this, handler.getLeft().read(buf));
    }

    public final void toBytes(PacketBuffer buf) {
        try {
            Class<?> clazz = getClass();
            for (Field f : clazz.getDeclaredFields()) {
                if (!f.isAccessible()) f.setAccessible(true);
                Class<?> type = f.getType();
                if (acceptField(f, type))
                    writeField(f, type, buf);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at writing packet " + this, e);
        }
    }

    public interface Writer<T> {
        void write(PacketBuffer buf, T t);
    }

    public interface Reader<T> {
        T read(PacketBuffer buf) throws IOException;
    }
}