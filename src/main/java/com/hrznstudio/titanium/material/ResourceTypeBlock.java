/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.api.material.IResourceHolder;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class ResourceTypeBlock extends BasicBlock implements IJsonFile, IJSONGenerator, IResourceHolder, IHasColor {

    private final ResourceMaterial resourceMaterial;
    private final IResourceType resourceType;
    private final IAdvancedResourceType blockResourceType;

    public ResourceTypeBlock(ResourceMaterial material, IResourceType type, IAdvancedResourceType blockType, ResourceTypeProperties<Properties> properties) {
        super(material.getMaterialType(), (properties == null ? ((ResourceTypeProperties<Properties>) ResourceTypeProperties.DEFAULTS.get(Block.class)).get() : properties.get())); //getName
        this.resourceMaterial = material;
        this.resourceType = type;
        this.blockResourceType = blockType;
        setItemGroup(ResourceRegistry.RESOURCES);
    }

    @Override
    public int getColor(int tintIndex) {
        return blockResourceType.getColor(resourceMaterial, tintIndex);
    }

    @Override
    public ResourceMaterial getMaterial() {
        return resourceMaterial;
    }

    @Override
    public IResourceType getType() {
        return resourceType;
    }

    @Override
    public JsonObject generate() {
        return blockResourceType.generate(resourceType);
    }

    @Override
    public String getRecipeKey() {
        return getRegistryName().getPath();
    }

    @Nullable
    @Override
    public String getRecipeSubfolder() {
        return "assets/" + Titanium.MODID + "/models/block/";
    }

    @Override
    public Supplier<Item> getItemBlockFactory() {
        return () -> (BlockItem) new BlockItem(this, new Item.Properties().tab(this.getItemGroup())) {
            @Override
            public Component getName(ItemStack p_200295_1_) {
                return resourceType.getTextComponent(resourceMaterial.getTextComponent());
            }
        }.setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

}
