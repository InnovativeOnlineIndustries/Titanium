/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.api.material.IResourceHolder;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.item.BasicItem;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class ResourceTypeItem extends BasicItem implements IJsonFile, IJSONGenerator, IResourceHolder, IHasColor {

    private final ResourceMaterial material;
    private final IResourceType type;
    private final IAdvancedResourceType advancedResourceType;

    public ResourceTypeItem(ResourceMaterial material, IResourceType type, IAdvancedResourceType advancedResourceType, ResourceTypeProperties<Properties> properties) {
        super(material.getMaterialType() + "_" + type.getString(), (properties == null ? ((ResourceTypeProperties<Properties>) ResourceTypeProperties.DEFAULTS.get(Item.class)).get() : properties.get())); //getName
        this.material = material;
        this.type = type;
        this.advancedResourceType = advancedResourceType;
    }

    public ResourceMaterial getMaterial() {
        return material;
    }

    public IResourceType getType() {
        return type;
    }

    @Override
    public JsonObject generate() {
        return advancedResourceType.generate(type);
    }

    @Override
    public String getRecipeKey() {
        return getRegistryName().getPath();
    }

    @Nullable
    @Override
    public String getRecipeSubfolder() {
        return "assets/" + Titanium.MODID + "/models/item/";
    }

    @Override
    public int getColor(int tintIndex) {
        return advancedResourceType.getColor(material, tintIndex);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack p_200295_1_) {
        return type.getTextComponent(material.getTextComponent());
    }
}
