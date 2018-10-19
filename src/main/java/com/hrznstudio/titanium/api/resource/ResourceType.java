/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.api.resource;

import com.hrznstudio.titanium.block.BlockOre;
import com.hrznstudio.titanium.block.BlockResource;
import com.hrznstudio.titanium.item.ItemResource;
import net.minecraft.util.IStringSerializable;

import java.util.function.Function;

//TODO: Make this an interface or something
public enum ResourceType implements IStringSerializable {
    INGOT(ItemResource::new),
    DUST(ItemResource::new),
    NUGGET(ItemResource::new),
    CHUNK(ItemResource::new),
    CLUMP(ItemResource::new),
    CRUSHED(ItemResource::new),
    PURIFIED(ItemResource::new),
    STONE(ItemResource::new),
    PEBBLES(ItemResource::new),
    FLAKES(ItemResource::new),
    GRINDINGS(ItemResource::new),
    SMALLDUST(ItemResource::new),
    PLATE(ItemResource::new),
    DENSE_PLATE(ItemResource::new, "plateDense"),
    CASING(ItemResource::new),
    REINFORCED_PLATE(ItemResource::new, "plateReinforced"),
    ROD(ItemResource::new), //TODO: Rod Textures
    DENSE_ROD(ItemResource::new, "rodDense"),
    GEAR(ItemResource::new),
    ORE(BlockOre::new);

    public static final ResourceType[] DEFAULT = new ResourceType[]{ORE, INGOT, DUST, NUGGET, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST, PLATE, DENSE_PLATE, CASING, REINFORCED_PLATE, GEAR};
    public static final ResourceType[] VANILLA = new ResourceType[]{DUST, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST, PLATE, DENSE_PLATE, CASING, REINFORCED_PLATE, GEAR};
    public static final ResourceType[] ORE_ONLY = new ResourceType[]{ORE, DUST, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST};

    private String oreDict;

    private boolean hasMaterial;
    private Function<ResourceType, ItemResource> itemFunction;
    private Function<ResourceMaterial, BlockResource> blockFunction;

    boolean hasItem;

    ResourceType(IItemFactory itemFunction) {
        this.itemFunction = itemFunction;
    }

    ResourceType(IBlockFactory blockFunction) {
        this.blockFunction = blockFunction;
    }

    ResourceType(IBlockFactory blockFunction, String oreDict) {
        this.blockFunction = blockFunction;
    }

    ResourceType(IItemFactory blockFunction, String oreDict) {
        this.itemFunction = blockFunction;
    }

    public String getOreDict() {
        return oreDict;
    }

    public boolean hasItem() {
        return hasItem && hasMaterial;
    }

    public void setMaterial() {
        this.hasMaterial = true;
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }

    public Function<ResourceType, ItemResource> getItemFunction() {
        return itemFunction;
    }

    public Function<ResourceMaterial, BlockResource> getBlockFunction() {
        return blockFunction;
    }

    public interface IItemFactory extends Function<ResourceType, ItemResource> {

    }

    public interface IBlockFactory extends Function<ResourceMaterial, BlockResource> {

    }
}