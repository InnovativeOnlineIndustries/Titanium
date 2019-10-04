/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.material.ResourceMaterial;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public interface IResourceType extends IStringSerializable {

    String getTag();

    IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material);

    default String getUnlocalizedName() {
        return String.format("resource.titanium.type.%s", getName());
    }
}
