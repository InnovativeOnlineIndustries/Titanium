/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.api.material.IResourceHolder;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.item.ItemBase;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ResourceTypeItem extends ItemBase implements IJsonFile, IJSONGenerator, IResourceHolder, IHasColor {

    private final ResourceMaterial material;
    private final IResourceType type;

    public ResourceTypeItem(ResourceMaterial material, IResourceType type) {
        super(material.getMaterialType() + "_" + type.getName(), new Properties().group(ResourceRegistry.RESOURCES));
        this.material = material;
        this.type = type;
    }

    public ResourceMaterial getMaterial() {
        return material;
    }

    public IResourceType getType() {
        return type;
    }

    @Override
    public JsonObject generate() {
        JsonObject object = new JsonObject();
        object.addProperty("parent", "item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", new ResourceLocation(Titanium.MODID, "items/resource/" + type.getName()).toString());
        object.add("textures", textures);
        return object;
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
        return material.getColor();
    }

    @Override
    public ITextComponent getDisplayName(ItemStack p_200295_1_) {
        return new TranslationTextComponent(type.getUnlocalizedName(), new TranslationTextComponent(material.getUnlocalizedName()));
    }
}
