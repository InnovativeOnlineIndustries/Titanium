/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
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
        map(byte.class, FriendlyByteBuf::readByte, (Writer<Byte>) FriendlyByteBuf::writeByte);
        map(short.class, FriendlyByteBuf::readShort, (Writer<Short>) FriendlyByteBuf::writeShort);
        map(int.class, FriendlyByteBuf::readVarInt, FriendlyByteBuf::writeVarInt);
        map(long.class, FriendlyByteBuf::readVarLong, FriendlyByteBuf::writeVarLong);
        map(float.class, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
        map(double.class, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
        map(boolean.class, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
        map(char.class, FriendlyByteBuf::readChar, (Writer<Character>) FriendlyByteBuf::writeChar);

        map(Byte.class, FriendlyByteBuf::readByte, (Writer<Byte>) FriendlyByteBuf::writeByte);
        map(Short.class, FriendlyByteBuf::readShort, (Writer<Short>) FriendlyByteBuf::writeShort);
        map(Integer.class, FriendlyByteBuf::readVarInt, FriendlyByteBuf::writeVarInt);
        map(Long.class, FriendlyByteBuf::readVarLong, FriendlyByteBuf::writeVarLong);
        map(Float.class, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
        map(Double.class, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
        map(Boolean.class, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
        map(Character.class, FriendlyByteBuf::readChar, (Writer<Character>) FriendlyByteBuf::writeChar);

        map(byte[].class, FriendlyByteBuf::readByteArray, FriendlyByteBuf::writeByteArray);
        map(int[].class, FriendlyByteBuf::readVarIntArray, FriendlyByteBuf::writeVarIntArray);
        map(long[].class, CompoundSerializableDataHandler::readLongArray, FriendlyByteBuf::writeLongArray);

        map(String.class, CompoundSerializableDataHandler::readString, FriendlyByteBuf::writeUtf);
        map(CompoundTag.class, FriendlyByteBuf::readNbt, FriendlyByteBuf::writeNbt);
        map(ItemStack.class, FriendlyByteBuf::readItem, FriendlyByteBuf::writeItem);
        map(FluidStack.class, CompoundSerializableDataHandler::readFluidStack, CompoundSerializableDataHandler::writeFluidStack);
        map(BlockPos.class, FriendlyByteBuf::readBlockPos, FriendlyByteBuf::writeBlockPos);
        map(Component.class, FriendlyByteBuf::readComponent, FriendlyByteBuf::writeComponent);
        map(Date.class, FriendlyByteBuf::readDate, FriendlyByteBuf::writeDate);
        map(UUID.class, FriendlyByteBuf::readUUID, FriendlyByteBuf::writeUUID);
        map(ClientboundBlockEntityDataPacket.class, CompoundSerializableDataHandler::readUpdatePacket, CompoundSerializableDataHandler::writeUpdatePacket);
        map(LocatorInstance.class, LocatorFactory::readPacketBuffer, LocatorFactory::writePacketBuffer);
        map(Ingredient.Value.class, CollectionItemList::new, CollectionItemList::serializeBuffer);
        map(Ingredient.class, Ingredient::fromNetwork, (buf, ingredient) -> ingredient.toNetwork(buf));
        map(Block.class, buf -> ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation()), (buf, block) -> buf.writeResourceLocation(block.getRegistryName()));
        map(Ingredient.Value[].class, CompoundSerializableDataHandler::readIItemListArray, CompoundSerializableDataHandler::writeIItemListArray);
        map(Ingredient[].class, CompoundSerializableDataHandler::readIngredientArray, CompoundSerializableDataHandler::writeIngredientArray);
        map(ResourceKey.class, CompoundSerializableDataHandler::readRegistryKey, CompoundSerializableDataHandler::writeRegistryKey);
        map(ResourceKey[].class, CompoundSerializableDataHandler::readRegistryArray, CompoundSerializableDataHandler::writeRegistryArray);
        map(ResourceLocation.class, FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);

    }

    public static <T> void map(Class<T> type, Reader<T> reader, Writer<T> writer) {
        FIELD_SERIALIZER.put(type, Pair.of(reader, writer));
    }

    private static long[] readLongArray(FriendlyByteBuf buf) {
        return buf.readLongArray(new long[0]);
    }

    private static String readString(FriendlyByteBuf buf) {
        return buf.readUtf(32767);
    }

    private static FluidStack readFluidStack(FriendlyByteBuf buf) throws IOException {
        return FluidStack.loadFluidStackFromNBT(buf.readNbt());
    }

    private static void writeFluidStack(FriendlyByteBuf buf, FluidStack stack) {
        buf.writeNbt(stack == null ? FluidStack.EMPTY.writeToNBT(new CompoundTag()) : stack.writeToNBT(new CompoundTag()));
    }

    public static ResourceKey<?> readRegistryKey(FriendlyByteBuf buffer) {
        return ResourceKey.create(ResourceKey.createRegistryKey(buffer.readResourceLocation()), buffer.readResourceLocation());
    }

    public static void writeRegistryKey(FriendlyByteBuf buffer, ResourceKey<?> biome) {
        buffer.writeResourceLocation(biome.getRegistryName());
        buffer.writeResourceLocation(biome.location());
    }

    private static void writeUpdatePacket(FriendlyByteBuf buf, ClientboundBlockEntityDataPacket packet) {
        try {
            packet.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Ingredient.Value[] readIItemListArray(FriendlyByteBuf buf) {
        Ingredient.Value[] list = new Ingredient.Value[buf.readInt()];
        for (int i = 0; i < list.length; i++) {
            list[i] = new CollectionItemList(buf);
        }
        return list;
    }

    private static void writeIItemListArray(FriendlyByteBuf buf, Ingredient.Value[] list) {
        buf.writeInt(list.length);
        for (Ingredient.Value iItemList : list) {
            CollectionItemList.serializeBuffer(buf, iItemList);
        }
    }

    public static ResourceKey<?>[] readRegistryArray(FriendlyByteBuf buffer) {
        ResourceKey[] registryKeys = new ResourceKey[buffer.readInt()];
        for (int i = 0; i < registryKeys.length; i++) {
            registryKeys[i] = readRegistryKey(buffer);
        }
        return registryKeys;
    }

    public static void writeRegistryArray(FriendlyByteBuf buffer, ResourceKey<?>[] registryKeys) {
        buffer.writeInt(registryKeys.length);
        for (ResourceKey<?> registryKey : registryKeys) {
            writeRegistryKey(buffer, registryKey);
        }
    }

    public static Ingredient[] readIngredientArray(FriendlyByteBuf buffer) {
        Ingredient[] ingredients = new Ingredient[buffer.readInt()];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = Ingredient.fromNetwork(buffer);
        }
        return ingredients;
    }

    public static void writeIngredientArray(FriendlyByteBuf buffer, Ingredient[] ingredients) {
        buffer.writeInt(ingredients.length);
        for (Ingredient ingredient : ingredients) {
            ingredient.toNetwork(buffer);
        }
    }

    private static ClientboundBlockEntityDataPacket readUpdatePacket(FriendlyByteBuf buf) {
        ClientboundBlockEntityDataPacket packet = new ClientboundBlockEntityDataPacket();
        try {
            packet.write(buf);
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

    public static void writeField(Field f, Class clazz, FriendlyByteBuf buf, Object instance) throws IllegalArgumentException, IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        handler.getRight().write(buf, f.get(instance));
    }

    public static void readField(Field f, Class clazz, FriendlyByteBuf buf, Object instance) throws IllegalArgumentException, IllegalAccessException, IOException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        f.set(instance, handler.getLeft().read(buf));
    }

    public static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) && getHandler(type) != null;
    }

    public interface Writer<T> {
        void write(FriendlyByteBuf buf, T t);
    }

    public interface Reader<T> {
        T read(FriendlyByteBuf buf) throws IOException;
    }

    public static class CollectionItemList implements Ingredient.Value {

        private List<ItemStack> stackList;

        public CollectionItemList(FriendlyByteBuf buffer) {
            this.stackList = new ArrayList<>();
            int amount = buffer.readInt();
            for (int i = 0; i < amount; i++) {
                stackList.add(buffer.readItem());
            }
        }

        public static void serializeBuffer(FriendlyByteBuf buffer, Ingredient.Value list) {
            buffer.writeInt(list.getItems().size());
            list.getItems().forEach(buffer::writeItem);
        }

        @Override
        public Collection<ItemStack> getItems() {
            return stackList;
        }

        @Override
        public JsonObject serialize() {
            return new JsonObject();
        }

    }
}
