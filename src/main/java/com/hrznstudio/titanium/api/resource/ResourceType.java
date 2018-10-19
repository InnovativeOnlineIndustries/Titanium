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
    INGOT((IItemFactory) material -> new ItemResource(ResourceType.INGOT, material)),
    DUST((IItemFactory) material -> new ItemResource(ResourceType.DUST, material)),
    NUGGET((IItemFactory) material -> new ItemResource(ResourceType.NUGGET, material)),
    CHUNK((IItemFactory) material -> new ItemResource(ResourceType.CHUNK, material)),
    CLUMP((IItemFactory) material -> new ItemResource(ResourceType.CLUMP, material)),
    CRUSHED((IItemFactory) material -> new ItemResource(ResourceType.CRUSHED, material)),
    PURIFIED((IItemFactory) material -> new ItemResource(ResourceType.PURIFIED, material)),
    STONE((IItemFactory) material -> new ItemResource(ResourceType.STONE, material)),
    PEBBLES((IItemFactory) material -> new ItemResource(ResourceType.PEBBLES, material)),
    FLAKES((IItemFactory) material -> new ItemResource(ResourceType.FLAKES, material)),
    GRINDINGS((IItemFactory) material -> new ItemResource(ResourceType.GRINDINGS, material)),
    SMALLDUST((IItemFactory) material -> new ItemResource(ResourceType.SMALLDUST, material)),
    PLATE((IItemFactory) material -> new ItemResource(ResourceType.PLATE, material)),
    DENSE_PLATE((IItemFactory) material -> new ItemResource(ResourceType.DENSE_PLATE, material), "plateDense"),
    CASING((IItemFactory) material -> new ItemResource(ResourceType.CASING, material)),
    REINFORCED_PLATE((IItemFactory) material -> new ItemResource(ResourceType.REINFORCED_PLATE, material), "plateReinforced"),
    ROD((IItemFactory) material -> new ItemResource(ResourceType.ROD, material)), //TODO: Rod Textures
    DENSE_ROD((IItemFactory) material -> new ItemResource(ResourceType.DENSE_ROD, material), "rodDense"),
    GEAR((IItemFactory) material -> new ItemResource(ResourceType.GEAR, material)),
    ORE(BlockOre::new);

    public static final ResourceType[] DEFAULT = new ResourceType[]{ORE, INGOT, DUST, NUGGET, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST, PLATE, DENSE_PLATE, CASING, REINFORCED_PLATE, GEAR};
    public static final ResourceType[] VANILLA = new ResourceType[]{DUST, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST, PLATE, DENSE_PLATE, CASING, REINFORCED_PLATE, GEAR};
    public static final ResourceType[] ORE_ONLY = new ResourceType[]{ORE, DUST, CHUNK, CLUMP, CRUSHED, PURIFIED, STONE, PEBBLES, FLAKES, GRINDINGS, SMALLDUST};

    private String oreDict;

    private boolean hasMaterial;
    private Function<ResourceMaterial, ItemResource> itemFunction;
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

    public Function<ResourceMaterial, ItemResource> getItemFunction() {
        return itemFunction;
    }

    public Function<ResourceMaterial, BlockResource> getBlockFunction() {
        return blockFunction;
    }

    public interface IItemFactory extends Function<ResourceMaterial, ItemResource> {

    }

    public interface IBlockFactory extends Function<ResourceMaterial, BlockResource> {

    }
}