/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.nbthandler;

import com.google.common.collect.Lists;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.nbthandler.data.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NBTManager {

    private static NBTManager ourInstance = new NBTManager();
    private List<INBTHandler> handlerList;
    private HashMap<Class<? extends TileEntity>, List<Field>> tileFieldList;

    private NBTManager() {
        handlerList = new ArrayList<>();
        tileFieldList = new HashMap<>();
        handlerList.add(new IntegerNBTHandler());
        handlerList.add(new ShortNBTHandler());
        handlerList.add(new LongNBTHandler());
        handlerList.add(new FloatNBTHandler());
        handlerList.add(new DoubleNBTHandler());
        handlerList.add(new BooleanNBTHandler());
        handlerList.add(new BlockPosNBTHandler());
        handlerList.add(new StringNBTHandler());
        handlerList.add(new ItemStackHandlerNBTHandler());
        handlerList.add(new TankNBTHandler());
        handlerList.add(new NBTSerializableNBTHandler());
    }

    public static NBTManager getInstance() {
        return ourInstance;
    }

    private static List<Field> getAllDeclaredFields(Class<?> entity) {
        List<Field> currentClassFields = Lists.newArrayList(entity.getDeclaredFields());
        Class<?> parent = entity.getSuperclass();
        if (TileEntity.class.isAssignableFrom(parent))
            currentClassFields.addAll(getAllDeclaredFields(entity.getSuperclass()));
        return currentClassFields;
    }

    /**
     * Scans a {@link TileEntity} class for {@link Save}.
     *
     * @param entity The TileEntity class
     */
    public void scanTileClassForAnnotations(Class<? extends TileEntity> entity) {
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
        for (INBTHandler handler : handlerList) {
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
    public NBTTagCompound writeTileEntity(TileEntity entity, NBTTagCompound compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                Save save = field.getAnnotation(Save.class);
                try {
                    if (field.get(entity) == null) continue;
                    compound = handleNBTWrite(compound, save.value().isEmpty() ? field.getName() : save.value(), field.get(entity));
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
    public void readTileEntity(TileEntity entity, NBTTagCompound compound) {
        if (tileFieldList.containsKey(entity.getClass())) {
            for (Field field : tileFieldList.get(entity.getClass())) {
                Save save = field.getAnnotation(Save.class);
                try {
                    Object value = handleNBTRead(compound, save.value().isEmpty() ? field.getName() : save.value(), field.get(entity));
                    field.set(entity, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks for a {@link INBTHandler} that can handle the write of the value and writes it to the NBTTagCompound.
     *
     * @param compound The NBTTagCompound to write the value.
     * @param name     The name of the tag for the NBTTagCompound.
     * @param value    The value to store in the NBTTagCompound.
     * @return the modified NBTTagCompound.
     */
    private NBTTagCompound handleNBTWrite(NBTTagCompound compound, String name, Object value) {
        for (INBTHandler handler : handlerList) {
            if (handler.isClassValid(value.getClass())) {
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
    private Object handleNBTRead(NBTTagCompound compound, String name, Object value) {
        for (INBTHandler handler : handlerList) {
            if (handler.isClassValid(value.getClass())) {
                if (!compound.hasKey(name)) continue;
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

    public HashMap<Class<? extends TileEntity>, List<Field>> getTileFieldList() {
        return tileFieldList;
    }
}
