package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import com.hrznstudio.titanium.api.resource.ResourceType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.function.Function;

public abstract class BlockResource extends BlockBase {
    private ResourceMaterial resourceMaterial;

    public BlockResource(ResourceMaterial resourceMaterial, Material material) {
        super(resourceMaterial.materialName, material);
        this.resourceMaterial = resourceMaterial;
    }

    public void registerModels() {
        ModelResourceLocation location = null;
        for (Function<ResourceType, ModelResourceLocation> function : resourceMaterial.getModelFunctions()) {
            location = function.apply(getType());
            if (location != null)
                break;
        }
        if (location != null) ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, location);
    }

    public ResourceMaterial getResourceMaterial() {
        return resourceMaterial;
    }

    public abstract ResourceType getType();
}
