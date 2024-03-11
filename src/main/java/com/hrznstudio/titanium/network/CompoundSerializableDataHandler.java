/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class CompoundSerializableDataHandler {

    private static final HashMap<Class, Pair<Reader, Writer>> FIELD_SERIALIZER = new HashMap<>();

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
        map(long[].class, CompoundSerializableDataHandler::readLongArray, PacketBuffer::writeLongArray);

        map(String.class, CompoundSerializableDataHandler::readString, PacketBuffer::writeString);
        map(CompoundNBT.class, PacketBuffer::readCompoundTag, PacketBuffer::writeCompoundTag);
        map(ItemStack.class, PacketBuffer::readItemStack, PacketBuffer::writeItemStack);
        map(FluidStack.class, CompoundSerializableDataHandler::readFluidStack, CompoundSerializableDataHandler::writeFluidStack);
        map(BlockPos.class, PacketBuffer::readBlockPos, PacketBuffer::writeBlockPos);
        map(ITextComponent.class, PacketBuffer::readTextComponent, PacketBuffer::writeTextComponent);
        map(Date.class, PacketBuffer::readTime, PacketBuffer::writeTime);
        map(UUID.class, PacketBuffer::readUniqueId, PacketBuffer::writeUniqueId);
        map(SUpdateTileEntityPacket.class, CompoundSerializableDataHandler::readUpdatePacket, CompoundSerializableDataHandler::writeUpdatePacket);
        map(LocatorInstance.class, LocatorFactory::readPacketBuffer, LocatorFactory::writePacketBuffer);
        map(Ingredient.IItemList.class, CollectionItemList::new, CollectionItemList::serializeBuffer);
        map(Ingredient.class, Ingredient::read, (buf, ingredient) -> ingredient.write(buf));
        map(Block.class, buf -> ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation()), (buf, block) -> buf.writeResourceLocation(block.getRegistryName()));
        map(Ingredient.IItemList[].class, CompoundSerializableDataHandler::readIItemListArray, CompoundSerializableDataHandler::writeIItemListArray);
        map(Ingredient[].class, CompoundSerializableDataHandler::readIngredientArray, CompoundSerializableDataHandler::writeIngredientArray);
        map(RegistryKey.class, CompoundSerializableDataHandler::readRegistryKey, CompoundSerializableDataHandler::writeRegistryKey);
        map(RegistryKey[].class, CompoundSerializableDataHandler::readRegistryArray, CompoundSerializableDataHandler::writeRegistryArray);
        map(ResourceLocation.class, PacketBuffer::readResourceLocation, PacketBuffer::writeResourceLocation);

    }

    public static <T> void map(Class<T> type, Reader<T> reader, Writer<T> writer) {
        FIELD_SERIALIZER.put(type, Pair.of(reader, writer));
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
        buf.writeCompoundTag(stack == null ? FluidStack.EMPTY.writeToNBT(new CompoundNBT()) : stack.writeToNBT(new CompoundNBT()));
    }

    public static RegistryKey<?> readRegistryKey(PacketBuffer buffer) {
        return RegistryKey.getOrCreateKey(RegistryKey.getOrCreateRootKey(buffer.readResourceLocation()), buffer.readResourceLocation());
    }

    public static void writeRegistryKey(PacketBuffer buffer, RegistryKey<?> biome) {
        buffer.writeResourceLocation(biome.getRegistryName());
        buffer.writeResourceLocation(biome.getLocation());
    }

    private static void writeUpdatePacket(PacketBuffer buf, SUpdateTileEntityPacket packet) {
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Ingredient.IItemList[] readIItemListArray(PacketBuffer buf) {
        Ingredient.IItemList[] list = new Ingredient.IItemList[buf.readInt()];
        for (int i = 0; i < list.length; i++) {
            list[i] = new CollectionItemList(buf);
        }
        return list;
    }

    private static void writeIItemListArray(PacketBuffer buf, Ingredient.IItemList[] list) {
        buf.writeInt(list.length);
        for (Ingredient.IItemList iItemList : list) {
            CollectionItemList.serializeBuffer(buf, iItemList);
        }
    }

    public static RegistryKey<?>[] readRegistryArray(PacketBuffer buffer) {
        RegistryKey[] registryKeys = new RegistryKey[buffer.readInt()];
        for (int i = 0; i < registryKeys.length; i++) {
            registryKeys[i] = readRegistryKey(buffer);
        }
        return registryKeys;
    }

    public static void writeRegistryArray(PacketBuffer buffer, RegistryKey<?>[] registryKeys) {
        buffer.writeInt(registryKeys.length);
        for (RegistryKey<?> registryKey : registryKeys) {
            writeRegistryKey(buffer, registryKey);
        }
    }

    public static Ingredient[] readIngredientArray(PacketBuffer buffer) {
        Ingredient[] ingredients = new Ingredient[buffer.readInt()];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = Ingredient.read(buffer);
        }
        return ingredients;
    }

    public static void writeIngredientArray(PacketBuffer buffer, Ingredient[] ingredients) {
        buffer.writeInt(ingredients.length);
        for (Ingredient ingredient : ingredients) {
            ingredient.write(buffer);
        }
    }

    private static SUpdateTileEntityPacket readUpdatePacket(PacketBuffer buf) {
        SUpdateTileEntityPacket packet = new SUpdateTileEntityPacket();
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Pair<Reader, Writer> getHandler(Class<?> clazz) {
        Pair<Reader, Writer> pair = FIELD_SERIALIZER.get(clazz);
        if (pair == null)
            throw new RuntimeException("No R/W handler for  " + clazz);
        return pair;
    }

    public static void writeField(Field f, Class clazz, PacketBuffer buf, Object instance) throws IllegalArgumentException, IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        handler.getRight().write(buf, f.get(instance));
    }

    public static void readField(Field f, Class clazz, PacketBuffer buf, Object instance) throws IllegalArgumentException, IllegalAccessException, IOException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        f.set(instance, handler.getLeft().read(buf));
    }

    public static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) && getHandler(type) != null;
    }

    public interface Writer<T> {
        void write(PacketBuffer buf, T t);
    }

    public interface Reader<T> {
        T read(PacketBuffer buf) throws IOException;
    }

    public static class CollectionItemList implements Ingredient.IItemList {

        private List<ItemStack> stackList;

        public CollectionItemList(PacketBuffer buffer) {
            this.stackList = new ArrayList<>();
            int amount = buffer.readInt();
            for (int i = 0; i < amount; i++) {
                stackList.add(buffer.readItemStack());
            }
        }

        public static void serializeBuffer(PacketBuffer buffer, Ingredient.IItemList list) {
            buffer.writeInt(list.getStacks().size());
            list.getStacks().forEach(buffer::writeItemStack);
        }

        @Override
        public Collection<ItemStack> getStacks() {
            return stackList;
        }

        @Override
        public JsonObject serialize() {
            return new JsonObject();
        }

    }
}
