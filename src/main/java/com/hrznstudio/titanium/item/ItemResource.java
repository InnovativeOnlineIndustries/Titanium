/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.item;

import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import com.hrznstudio.titanium.api.resource.ResourceType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class ItemResource extends ItemBase {
    private ResourceType type;
    private ResourceMaterial material;

    public ItemResource(ResourceType type, ResourceMaterial material) {
        super(material.materialName + "_" + type.getName());
        this.type = type;
        this.material = material;
    }

    public ResourceType getType() {
        return type;
    }

    public void registerModels() {
        ModelResourceLocation location = null;
        for (Function<ResourceType, ModelResourceLocation> function : material.getModelFunctions()) {
            location = function.apply(type);
            if (location != null)
                break;
        }
        if (location != null) ModelLoader.setCustomModelResourceLocation(this, 0, location);
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(ItemStack stack) {
        return String.format(super.getItemStackDisplayName(stack), material.getLocalizedName());
    }

    public ItemStack getStack(int amount) {
        return new ItemStack(this, amount);
    }
}