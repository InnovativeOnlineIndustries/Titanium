/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import com.hrznstudio.titanium.api.resource.ResourceType;
import com.hrznstudio.titanium.util.ClientUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Function;

public abstract class BlockResource extends BlockBase {
    private ResourceMaterial resourceMaterial;
    private ResourceType type;


    public BlockResource(ResourceMaterial resourceMaterial, ResourceType type, Material material) {
        super(resourceMaterial.materialName+"_"+type.getName(), material);
        this.resourceMaterial = resourceMaterial;
        this.type=type;
    }

    public void registerModels() {
        ModelResourceLocation location = null;
        for (Function<ResourceType, ModelResourceLocation> function : resourceMaterial.getModelFunctions()) {
            location = function.apply(getType());
            if (location != null)
                break;
        }
        if (location != null) ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, location);
        if (location != null) {
            ModelResourceLocation finalLocation = location;
            ClientUtil.setCustomStateMapper(this, state -> finalLocation);
        }
    }

    public ResourceMaterial getResourceMaterial() {
        return resourceMaterial;
    }

    public ResourceType getType() {
        return type;
    }
}
