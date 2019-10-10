/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.material.IResourceType;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public enum ResourceType implements IResourceType, IStringSerializable {
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
    GEM,
    SMALL_DUST("small_dust"),
    PLATE,
    DENSE_PLATE("plate_dense"),
    CASING,
    REINFORCED_PLATE("plate_reinforced"),
    ROD,
    DENSE_ROD("rod_dense"),
    GEAR,
    METAL_BLOCK("storage_blocks") {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, ResourceTypeBlock.BlockResourceType.METAL_BLOCK, properties);
        }
    },
    ORE {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, ResourceTypeBlock.BlockResourceType.ORE, properties);
        }
    },
    NETHER_ORE("ore/nether") {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, ResourceTypeBlock.BlockResourceType.NETHER_ORE, properties);
        }
    },
    GEM_BLOCK("storage_blocks") {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, ResourceTypeBlock.BlockResourceType.GEM_BLOCK, properties);
        }
    };

    private final String tag;

    ResourceType(String tag) {
        this.tag = tag;
    }

    ResourceType() {
        this.tag = getName();
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
        return () -> new ResourceTypeItem(material, this, properties);
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
