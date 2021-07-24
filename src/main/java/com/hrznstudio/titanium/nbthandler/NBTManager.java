/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.INBTHandler;
import com.hrznstudio.titanium.nbthandler.data.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NBTManager {

    private static NBTManager ourInstance = new NBTManager();
    private List<INBTHandler> handlerList;
    private HashMap<Class<? extends BlockEntity>, List<Field>> tileFieldList;

    private NBTManager() {
        handlerList = new ArrayList<>();
        tileFieldList = new HashMap<>();
        handlerList.add(new IntegerNBTHandler());
        handlerList.add(new ShortNBTHandler());
        handlerList.add(new LongNBTHandler());
        handlerList.add(new FloatNBTHandler());
        handlerList.add(new DoubleNBTHandler());
        handlerList.add(new BooleanNBTHandler());
        handlerList.add(new ItemStackNBTHandler());
        handlerList.add(new BlockPosNBTHandler());
        handlerList.add(new StringNBTHandler());
        handlerList.add(new EnumDyeColorNBTHandler());
        handlerList.add(new ItemStackHandlerNBTHandler());
        handlerList.add(new TankNBTHandler());
        handlerList.add(new UUIDNBTHandler());
        handlerList.add(new NBTSerializableNBTHandler());
    }

    public static NBTManager getInstance() {
        return ourInstance;
    }

    private static List<Field> getAllDeclaredFields(Class<?> entity) {
        List<Field> currentClassFields = Lists.newArrayList(entity.getDeclaredFields());
        Class<?> parent = entity.getSuperclass();
        if (BlockEntity.class.isAssignableFrom(parent))
            currentClassFields.addAll(getAllDeclaredFields(entity.getSuperclass()));
        return currentClassFields;
    }

    /**
     * Scans a {@link TileEntity} class for {@link Save}.
     *
     * @param entity The TileEntity class
     */
    public void scanTileClassForAnnotations(Class<? extends BlockEntity> entity) {
        List<Field> fields = new ArrayList<>();
        for (Field field : getAllDeclaredFields(entity)) {
            if (field.isAnnotationPresent(Save.class) && checkForHandler(field)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        for (Field field : entity.getFields()) {
            if (field.isAnnotationPresent(Save.class) && checkForHandler(field)) {
                fields.add(field);
            }
        }
        if (!fields.isEmpty()) tileFieldList.put(entity, fields);
    }

    private boolean checkForHandler(Field field) {
        for (INBTHandler<?> handler : handlerList) {
            if (handler.isClassValid(field.getType())) {
                return true;
            }
        }
        throw new RuntimeException("Missing NBT Field processor for " + field.getType());
    }

    /**
     * Writes all the values that have {@link Save} annotation to the NBTTagCompound.
     *
     * @param entity   The tile entity instance.
     * @param compound The NBTTagCompound to save the values.
     * @return the modified NBTTagCompound.
     */
    public CompoundTag writeTileEntity(BlockEntity entity, CompoundTag compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                Save save = field.getAnnotation(Save.class);
                try {
                    Object obj = field.get(entity);
                    if (obj == null) continue;
                    compound = handleNBTWrite(compound, save.value().isEmpty() ? field.getName() : save.value(), obj, field);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }

    /**
     * Reads all the values from the NBTTagCompound and saves it to the Fields.
     *
     * @param entity   The tile entity instance.
     * @param compound The NBTTagCompound to save the values.
     */
    public void readTileEntity(BlockEntity entity, CompoundTag compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                if (compound.contains(field.getName())){
                    Save save = field.getAnnotation(Save.class);
                    try {
                        Object value = handleNBTRead(compound, save.value().isEmpty() ? field.getName() : save.value(), field.get(entity), field);
                        field.set(entity, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Writes an specific object to be synced to the client
     * @param entity The tile entity instance
     * @param object The object to be synced
     * @param compound The NBTTagCompound to save the values
     * @return the modified NBTTagCompound.
     */
    public CompoundTag writeTileEntityObject(BlockEntity entity, Object object, CompoundTag compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                try {
                    if (object.equals(field.get(entity))) {
                        Save save = field.getAnnotation(Save.class);
                        Object obj = field.get(entity);
                        if (obj == null) continue;
                        compound = handleNBTWrite(compound, save.value().isEmpty() ? field.getName() : save.value(), obj, field);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return compound;
    }

    /**
     * Checks for a {@link INBTHandler} that can handle the write of the value and writes it to the NBTTagCompound.
     *
     * @param compound The NBTTagCompound to write the value.
     * @param name     The name of the tag for the NBTTagCompound.
     * @param value    The value to store in the NBTTagCompound.
     * @return the modified NBTTagCompound.
     */
    private CompoundTag handleNBTWrite(CompoundTag compound, String name, Object value, Field field) {
        for (INBTHandler handler : handlerList) {
            if (handler.isClassValid(value == null ? field.getType() : value.getClass())) {
                if (handler.storeToNBT(compound, name, value)) return compound;
            }
        }
        return compound;
    }

    /**
     * Checks for a {@link INBTHandler} that can handle the read of the value from the NBTTagCompound.
     *
     * @param compound The NBTTagCompound to read the value.
     * @param name     The name of the tag for the NBTTagCompound.
     * @param value    The current value.
     * @return
     */
    private Object handleNBTRead(CompoundTag compound, String name, @Nullable Object value, Field field) {
        for (INBTHandler handler : handlerList) {
            if (handler.isClassValid(value == null ? field.getType() : value.getClass())) {
                if (!compound.contains(name)) continue;
                Object readValue = handler.readFromNBT(compound, name, value);
                if (readValue != null) {
                    return readValue;
                }
            }
        }
        return value;
    }

    public List<INBTHandler> getHandlerList() {
        return handlerList;
    }

    public HashMap<Class<? extends BlockEntity>, List<Field>> getTileFieldList() {
        return tileFieldList;
    }
}
