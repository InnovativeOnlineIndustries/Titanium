/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.hrznstudio.titanium.Titanium;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;

// TODO - this really doesn't work anymore, codecs ftw
public class JSONSerializableDataHandler {

    private static HashMap<Class, Pair<Writer, Reader>> FIELD_SERIALIZER = new HashMap<>();

    static {
        map(byte.class, JsonPrimitive::new, JsonElement::getAsByte);
        map(short.class, JsonPrimitive::new, JsonElement::getAsShort);
        map(int.class, JsonPrimitive::new, JsonElement::getAsInt);
        map(long.class, JsonPrimitive::new, JsonElement::getAsLong);
        map(float.class, JsonPrimitive::new, JsonElement::getAsFloat);
        map(double.class, JsonPrimitive::new, JsonElement::getAsDouble);
        map(boolean.class, JsonPrimitive::new, JsonElement::getAsBoolean);
        map(char.class, JsonPrimitive::new, JsonElement::getAsCharacter);
        map(Byte.class, JsonPrimitive::new, JsonElement::getAsByte);
        map(Short.class, JsonPrimitive::new, JsonElement::getAsShort);
        map(Integer.class, JsonPrimitive::new, JsonElement::getAsInt);
        map(Long.class, JsonPrimitive::new, JsonElement::getAsLong);
        map(Float.class, JsonPrimitive::new, JsonElement::getAsFloat);
        map(Double.class, JsonPrimitive::new, JsonElement::getAsDouble);
        map(Boolean.class, JsonPrimitive::new, JsonElement::getAsBoolean);
        map(Character.class, JsonPrimitive::new, JsonElement::getAsCharacter);
        map(String.class, JsonPrimitive::new, JsonElement::getAsString);


        map(ItemStack.class, JSONSerializableDataHandler::writeItemStack, element -> readItemStack(element.getAsJsonObject()));
        map(ItemStack[].class, (stacks) -> {
            JsonArray array = new JsonArray();
            for (ItemStack stack : stacks) {
                array.add(JSONSerializableDataHandler.writeItemStack(stack));
            }
            return array;
        }, (element) -> {
            JsonArray array = element.getAsJsonArray();
            ItemStack[] stacks = new ItemStack[array.size()];
            for (int i = 0; i < array.size(); i++) {
                stacks[i] = JSONSerializableDataHandler.readItemStack(array.get(i).getAsJsonObject());
            }
            return stacks;
        });
        map(Identifier.class, type -> new JsonPrimitive(type.toString()), element -> Identifier.parse(element.getAsString()));
        map(Block.class, type -> new JsonPrimitive(BuiltInRegistries.BLOCK.getKey(type).toString()), element -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(element.getAsString())));
        map(FluidStack.class, JSONSerializableDataHandler::writeFluidStack, JSONSerializableDataHandler::readFluidStack);

        map(ResourceKey.class, JSONSerializableDataHandler::writeRegistryKey, JSONSerializableDataHandler::readRegistryKey);
        map(ResourceKey[].class, (registryKeys) -> {
            JsonObject object = new JsonObject();
            if (registryKeys.length > 0) {
                object.addProperty("type", registryKeys[0].registry().toString());
                JsonArray array = new JsonArray();
                for (ResourceKey registryKey : registryKeys) {
                    array.add(registryKey.identifier().toString());
                }
                object.add("values", array);
            }
            return object;
        }, element -> {
            ResourceKey[] registryKeys = new ResourceKey[0];
            if (element.getAsJsonObject().has("type")) {
                registryKeys = new ResourceKey[element.getAsJsonObject().getAsJsonArray("values").size()];
                int i = 0;
                for (Iterator<JsonElement> iterator = element.getAsJsonObject().getAsJsonArray("values").iterator(); iterator.hasNext(); i++) {
                    JsonElement jsonElement = iterator.next();
                    registryKeys[i] = ResourceKey.create(ResourceKey.createRegistryKey(Identifier.parse(element.getAsJsonObject().get("type").getAsString())), Identifier.parse(jsonElement.getAsString()));
                }
            }
            return registryKeys;
        });
//        map(Ingredient.class, (type) -> {
//            if (Ingredient.EMPTY.equals(type)) {
//                return null;
//            }
//            return Ingredient.CODEC.encodeStart(JsonOps.INSTANCE, type).getOrThrow(false, err -> {});
//        }, element -> Ingredient.CODEC.decode(JsonOps.INSTANCE, element.getAsJsonObject()).result().get().getFirst());
        map(Ingredient[].class, (type) -> {
            JsonArray array = new JsonArray();
            for (Ingredient ingredient : type) {
                array.add(write(Ingredient.class, ingredient));
            }
            return array;
        }, (element) -> {
            Ingredient[] ingredients = new Ingredient[element.getAsJsonArray().size()];
            int i = 0;
            for (Iterator<JsonElement> iterator = element.getAsJsonArray().iterator(); iterator.hasNext(); i++) {
                JsonElement jsonElement = iterator.next();
                ingredients[i] = read(Ingredient.class, jsonElement);
            }
            return ingredients;
        });
        map(CompoundTag.class, type -> new JsonPrimitive(type.toString()), element -> {
            try {
                return TagParser.parseCompoundFully(element.getAsString());
            } catch (CommandSyntaxException e) {
                Titanium.LOGGER.catching(e);
            }
            return new CompoundTag();
        });
    }

    public static <T> void map(Class<T> type, Writer<T> writer, Reader<T> reader) {
        FIELD_SERIALIZER.put(type, Pair.of(writer, reader));
    }

    public static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) && FIELD_SERIALIZER.containsKey(type);
    }

    public static <T> T read(Class<T> type, JsonElement element) {
        return (T) FIELD_SERIALIZER.get(type).getSecond().read(element);
    }

    public static <T> T readCodec(Codec<T> codec, JsonElement element) {
        return codec.decode(JsonOps.INSTANCE, element).result().orElseThrow().getFirst();
    }

    public static <T> JsonElement writeCodec(Codec<T> codec, T value) {
        return codec.encodeStart(JsonOps.INSTANCE, value).result().orElseThrow();
    }

    public static JsonElement write(Class<?> type, Object value) {
        return FIELD_SERIALIZER.get(type).getFirst().write(value);
    }

    public static JsonObject writeItemStack(ItemStack stack) {
        if(stack.isEmpty()) {
            return null;
        }
        JsonObject object = new JsonObject();
//        object.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
//        object.addProperty("count", stack.getCount());
//        if (stack.hasTag()) {
//            object.addProperty("nbt", stack.getTag().toString());
//        }
        return object;
    }

    public static JsonElement writeFluidStack(FluidStack fluidStack) {
        if(fluidStack.isEmpty()) {
            return null;
        }
        return new JsonObject();
//        return new JsonPrimitive(fluidStack.writeToNBT(new CompoundTag()).toString());
    }

    public static FluidStack readFluidStack(JsonElement object) {
//        try {
//            return FluidStack.loadFluidStackFromNBT(TagParser.parseTag(object.getAsString()));
//        } catch (CommandSyntaxException e) {
//            Titanium.LOGGER.catching(e);
//        }
        return FluidStack.EMPTY;
    }

    public static ItemStack readItemStack(JsonObject object) {
//        if (object.has("item")) {
//            object.add("id", object.get("item"));
//        }
//        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(Identifier.parse(object.get("item").getAsString())),
//                GsonHelper.getAsInt(object, "count", 1));
//        if (object.has("nbt")) {
//            try {
//                stack.setTag(TagParser.parseTag(object.get("nbt").getAsString()));
//            } catch (CommandSyntaxException e) {
//                Titanium.LOGGER.catching(e);
//            }
//        }
//        return stack;
        return ItemStack.EMPTY;
    }

    public static JsonObject writeRegistryKey(ResourceKey<?> registryKey) {
        JsonObject object = new JsonObject();
        object.addProperty("key", registryKey.registry().toString());
        object.addProperty("value", registryKey.identifier().toString());
        return object;
    }

    public static ResourceKey<?> readRegistryKey(JsonElement object) {
        return ResourceKey.create(ResourceKey.createRegistryKey(Identifier.parse(object.getAsJsonObject().get("key").getAsString())), Identifier.parse(object.getAsJsonObject().get("value").getAsString()));
    }

    public interface Writer<T> {
        JsonElement write(T type);
    }

    public interface Reader<T> {
        T read(JsonElement element);
    }
}
