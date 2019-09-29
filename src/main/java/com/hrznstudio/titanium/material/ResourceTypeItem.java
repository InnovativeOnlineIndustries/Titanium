package com.hrznstudio.titanium.material;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.api.material.IResourceHolder;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.item.ItemBase;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ResourceTypeItem extends ItemBase implements IJsonFile, IJSONGenerator, IResourceHolder, IHasColor {

    public static AdvancedTitaniumTab RESOURCES = new AdvancedTitaniumTab("resources", true);

    private final ResourceMaterial material;
    private final ResourceType type;

    public ResourceTypeItem(ResourceMaterial material, ResourceType type) {
        super(material.getMaterialType() + "_" + type.getName(), new Properties());
        this.material = material;
        this.type = type;
        setItemGroup(RESOURCES);
        RESOURCES.addIconStack(new ItemStack(this));
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
    public int getColor() {
        return material.getColor();
    }
}
