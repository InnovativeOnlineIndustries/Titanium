/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.api.resource;

import net.minecraft.util.IStringSerializable;

//TODO: Make this an interface or something
public enum ResourceType implements IStringSerializable {
    INGOT,
    DUST,
    NUGGET,
    CHUNK,
    CLUMP,
    CRUSHED,
    PURIFIED,
    STONE,
    PEBBLES,
    FLAKES,
    GRINDINGS,
    SMALLDUST,
    PLATE,
    DENSE_PLATE("plateDense"),
    CASING,
    REINFORCED_PLATE("plateReinforced"),
    ROD,
    DENSE_ROD("rodDense"),
    GEAR,
    FLUID;

    public static final ResourceType[] DEFAULT = new ResourceType[]{INGOT, DUST, NUGGET, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST, PLATE, DENSE_PLATE, CASING, REINFORCED_PLATE, ROD, DENSE_ROD, GEAR};
    public static final ResourceType[] VANILLA = new ResourceType[]{DUST, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST, PLATE, DENSE_PLATE, CASING, REINFORCED_PLATE, ROD, DENSE_ROD, GEAR};
    public static final ResourceType[] ORE = new ResourceType[]{DUST, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST};

    private String oreDict;

    private boolean hasMaterial;

    ResourceType(String oreDict) {
        this.oreDict = oreDict;
    }

    ResourceType() {
        this.oreDict = getName();
    }

    public String getOreDict() {
        return oreDict;
    }

    public boolean hasMaterial() {
        return hasMaterial;
    }

    public ResourceType setMaterial() {
        this.hasMaterial = true;
        return this;
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}