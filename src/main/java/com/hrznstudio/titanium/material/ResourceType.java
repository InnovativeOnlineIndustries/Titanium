/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.material.advancedtype.BlockAdvancedResourceType;
import com.hrznstudio.titanium.material.advancedtype.ItemAdvancedResourceType;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public enum ResourceType implements IResourceType, IStringSerializable {
    INGOT("ingots"),
    DUST("dusts"),
    NUGGET("nuggets"),
    CHUNK("chunks"),
    CLUMP("clumps"),
    CRUSHED,
    PURIFIED,
    STONE("stones"),
    PEBBLES,
    FLAKES,
    GRINDINGS,
    GEM("gems"),
    SMALL_DUST("small_dusts"),
    PLATE("plates"),
    DENSE_PLATE("plates_dense"),
    CASING("casings"),
    REINFORCED_PLATE("plates_reinforced"),
    ROD("rods"),
    DENSE_ROD("rods_dense"),
    GEAR("gears"),
    METAL_BLOCK("storage_blocks") {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, BlockAdvancedResourceType.METAL_BLOCK, properties);
        }
    },
    ORE {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, BlockAdvancedResourceType.ORE, properties);
        }
    },
    NETHER_ORE("ores/nether") {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, BlockAdvancedResourceType.NETHER_ORE, properties);
        }
    },
    GEM_BLOCK("storage_blocks") {
        @Override
        public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
            return () -> new ResourceTypeBlock(material, this, BlockAdvancedResourceType.GEM_BLOCK, properties);
        }
    };

    private final String tag;

    ResourceType(String tag) {
        this.tag = tag;
    }

    ResourceType() {
        this.tag = getString(); //GetName
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties) {
        return () -> new ResourceTypeItem(material, this, ItemAdvancedResourceType.SIMPLE, properties);
    }

    @Override
    public String getString() {
        return name().toLowerCase();
    }
}
