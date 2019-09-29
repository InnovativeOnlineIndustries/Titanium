package com.hrznstudio.titanium.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.material.IResourceType;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
    SMALL_DUST("small_dust"),
    PLATE,
    DENSE_PLATE("plate_dense"),
    CASING,
    REINFORCED_PLATE("plate_reinforced"),
    ROD,
    DENSE_ROD("rod_dense"),
    GEAR;

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
    public IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material) {
        return () -> new ResourceTypeItem(material, this);
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
