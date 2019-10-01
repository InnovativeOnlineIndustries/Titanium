package com.hrznstudio.titanium.api.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.material.ResourceMaterial;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public interface IResourceType extends IStringSerializable {

    String getTag();

    IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material);

}
