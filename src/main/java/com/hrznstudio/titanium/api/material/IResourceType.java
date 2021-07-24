/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.material;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.material.ResourceMaterial;
import com.hrznstudio.titanium.material.ResourceTypeProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public interface IResourceType extends StringRepresentable {

    String getTag();

    IFactory<ForgeRegistryEntry> getInstanceFactory(ResourceMaterial material, @Nullable ResourceTypeProperties properties);

    default Component getTextComponent(Component material) {
        return new TranslatableComponent(String.format("resource.titanium.type.%s", getSerializedName()), material); //getName
    }
}
