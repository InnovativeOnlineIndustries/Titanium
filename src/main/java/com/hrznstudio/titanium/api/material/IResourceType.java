package com.hrznstudio.titanium.api.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.material.ResourceMaterial;
import net.minecraftforge.registries.ForgeRegistryEntry;

public interface IResourceType {

    String getTag();

    IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material);

}
