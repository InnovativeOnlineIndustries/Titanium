/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network;

import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;
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

        map(byte[].class, (RegistryFriendlyByteBuf buf) -> buf.readByteArray(), RegistryFriendlyByteBuf::writeBytes);
        map(int[].class, FriendlyByteBuf::readVarIntArray, FriendlyByteBuf::writeVarIntArray);
        map(long[].class, CompoundSerializableDataHandler::readLongArray, FriendlyByteBuf::writeLongArray);

        map(String.class, CompoundSerializableDataHandler::readString, FriendlyByteBuf::writeUtf);
        map(CompoundTag.class, ByteBufCodecs.TRUSTED_COMPOUND_TAG);
        map(ItemStack.class, ItemStack.STREAM_CODEC);
        map(FluidStack.class, CompoundSerializableDataHandler::readFluidStack, CompoundSerializableDataHandler::writeFluidStack);
        map(BlockPos.class, BlockPos.STREAM_CODEC);
        map(Component.class, ComponentSerialization.STREAM_CODEC);
        map(Date.class, FriendlyByteBuf::readDate, FriendlyByteBuf::writeDate);
        map(UUID.class, UUIDUtil.STREAM_CODEC);
        map(ClientboundBlockEntityDataPacket.class, CompoundSerializableDataHandler::readUpdatePacket, CompoundSerializableDataHandler::writeUpdatePacket);
        map(LocatorInstance.class, LocatorFactory::readPacketBuffer, LocatorFactory::writePacketBuffer);
        map(Ingredient.Value.class, CollectionItemList::new, CollectionItemList::serializeBuffer);
        map(Ingredient.class, Ingredient.CONTENTS_STREAM_CODEC);
        map(Block.class, buf -> BuiltInRegistries.BLOCK.get(buf.readResourceLocation()), (buf, block) -> buf.writeResourceLocation(BuiltInRegistries.BLOCK.getKey(block)));
        map(Ingredient.Value[].class, CompoundSerializableDataHandler::readIItemListArray, CompoundSerializableDataHandler::writeIItemListArray);
        map(Ingredient[].class, CompoundSerializableDataHandler::readIngredientArray, CompoundSerializableDataHandler::writeIngredientArray);
        map(ResourceKey.class, CompoundSerializableDataHandler::readRegistryKey, CompoundSerializableDataHandler::writeRegistryKey);
        map(ResourceKey[].class, CompoundSerializableDataHandler::readRegistryArray, CompoundSerializableDataHandler::writeRegistryArray);
        map(ResourceLocation.class, FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);

    }

    public static <T> void map(Class<T> type, StreamCodec<? extends ByteBuf, T> codec) {
        var raw = (StreamCodec) codec;
        FIELD_SERIALIZER.put(type, Pair.of(buf -> raw.decode(buf), (buf, object) -> raw.encode(buf, object)));
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

    private static FluidStack readFluidStack(RegistryFriendlyByteBuf buf) throws IOException {
        return FluidStack.STREAM_CODEC.decode(buf);
    }

    private static void writeFluidStack(RegistryFriendlyByteBuf buf, FluidStack stack) {
        FluidStack.STREAM_CODEC.encode(buf, stack);
    }

    public static ResourceKey<?> readRegistryKey(FriendlyByteBuf buffer) {
        return ResourceKey.create(ResourceKey.createRegistryKey(buffer.readResourceLocation()), buffer.readResourceLocation());
    }

    public static void writeRegistryKey(FriendlyByteBuf buffer, ResourceKey<?> biome) {
        buffer.writeResourceLocation(biome.registry());
        buffer.writeResourceLocation(biome.location());
    }

    private static void writeUpdatePacket(RegistryFriendlyByteBuf buf, ClientboundBlockEntityDataPacket packet) {
        ClientboundBlockEntityDataPacket.STREAM_CODEC.encode(buf, packet);
    }

    private static Ingredient.Value[] readIItemListArray(RegistryFriendlyByteBuf buf) {
        Ingredient.Value[] list = new Ingredient.Value[buf.readInt()];
        for (int i = 0; i < list.length; i++) {
            list[i] = new CollectionItemList(buf);
        }
        return list;
    }

    private static void writeIItemListArray(RegistryFriendlyByteBuf buf, Ingredient.Value[] list) {
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

    public static Ingredient[] readIngredientArray(RegistryFriendlyByteBuf buffer) {
        Ingredient[] ingredients = new Ingredient[buffer.readInt()];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        }
        return ingredients;
    }

    public static void writeIngredientArray(RegistryFriendlyByteBuf buffer, Ingredient[] ingredients) {
        buffer.writeInt(ingredients.length);
        for (Ingredient ingredient : ingredients) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
    }

    private static ClientboundBlockEntityDataPacket readUpdatePacket(RegistryFriendlyByteBuf buf) {
        return ClientboundBlockEntityDataPacket.STREAM_CODEC.decode(buf);
    }

    public static Pair<Reader, Writer> getHandler(Class<?> clazz) {
        Pair<Reader, Writer> pair = FIELD_SERIALIZER.get(clazz);
        if (pair == null)
            throw new RuntimeException("No R/W handler for  " + clazz);
        return pair;
    }

    public static void writeField(Field f, Class clazz, RegistryFriendlyByteBuf buf, Object instance) throws IllegalArgumentException, IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        handler.getRight().write(buf, f.get(instance));
    }

    public static void readField(Field f, Class clazz, RegistryFriendlyByteBuf buf, Object instance) throws IllegalArgumentException, IllegalAccessException, IOException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        f.set(instance, handler.getLeft().read(buf));
    }

    public static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) && getHandler(type) != null;
    }

    public interface Writer<T> {
        void write(RegistryFriendlyByteBuf buf, T t);
    }

    public interface Reader<T> {
        T read(RegistryFriendlyByteBuf buf) throws IOException;
    }

    public static class CollectionItemList implements Ingredient.Value {

        private List<ItemStack> stackList;

        public CollectionItemList(RegistryFriendlyByteBuf buffer) {
            this.stackList = new ArrayList<>();
            int amount = buffer.readInt();
            for (int i = 0; i < amount; i++) {
                stackList.add(ItemStack.STREAM_CODEC.decode(buffer));
            }
        }

        public static void serializeBuffer(RegistryFriendlyByteBuf buffer, Ingredient.Value list) {
            buffer.writeInt(list.getItems().size());
            list.getItems().forEach(stack -> ItemStack.STREAM_CODEC.encode(buffer, stack));
        }

        @Override
        public Collection<ItemStack> getItems() {
            return stackList;
        }

    }
}
