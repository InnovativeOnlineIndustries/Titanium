/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.serializer;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

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

        map(Ingredient.class, Ingredient::serialize, Ingredient::deserialize);
        map(ItemStack.class, JSONSerializableDataHandler::writeItemStack, element -> readItemStack(element.getAsJsonObject()));
        map(ResourceLocation.class, type -> new JsonPrimitive(type.toString()), element -> new ResourceLocation(element.getAsString()));
        map(Block.class, type -> new JsonPrimitive(type.getRegistryName().toString()), element -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(element.getAsString())));
        map(FluidStack.class, JSONSerializableDataHandler::writeFluidStack, element -> readFluidStack(element.getAsJsonObject()));
    }

    private static <T> void map(Class<T> type, Writer<T> writer, Reader<T> reader) {
        FIELD_SERIALIZER.put(type, Pair.of(writer, reader));
    }

    public static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        return !Modifier.isFinal(mods) && !Modifier.isStatic(mods) && !Modifier.isTransient(mods) && FIELD_SERIALIZER.containsKey(type);
    }

    public static <T> T read(Class<T> type, JsonElement element) {
        return (T) FIELD_SERIALIZER.get(type).getSecond().read(element);
    }

    public static JsonElement write(Class<?> type, Object value) {
        return FIELD_SERIALIZER.get(type).getFirst().write(value);
    }

    public static JsonObject writeItemStack(ItemStack stack) {
        JsonObject object = new JsonObject();
        object.addProperty("item", stack.getItem().getRegistryName().toString());
        object.addProperty("count", stack.getCount());
        if (stack.hasTag()) object.addProperty("nbt", stack.getTag().toString());
        return object;
        //throw new UnsupportedOperationException("ItemStacks with nbt aren't implemented yet. Please do.");
        //net.minecraftforge.common.crafting.CraftingHelper.getItemStack
    }

    public static JsonElement writeFluidStack(FluidStack fluidStack) {
        return new JsonPrimitive(fluidStack.writeToNBT(new CompoundNBT()).toString());
    }

    public static FluidStack readFluidStack(JsonObject object) {
        try {
            return FluidStack.loadFluidStackFromNBT(JsonToNBT.getTagFromJson(object.getAsString()));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return FluidStack.EMPTY;
    }

    public static ItemStack readItemStack(JsonObject object) {
        ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(object.get("item").getAsString())), object.get("count").getAsInt());
        if (object.has("nbt")) {
            try {
                stack.setTag(JsonToNBT.getTagFromJson(object.get("nbt").getAsString()));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
        return stack;
    }

    public interface Writer<T> {
        JsonElement write(T type);
    }

    public interface Reader<T> {
        T read(JsonElement element);
    }
}
