/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.material;

import com.google.gson.JsonObject;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.material.IHasColor;
import com.hrznstudio.titanium.api.material.IResourceHolder;
import com.hrznstudio.titanium.api.material.IResourceType;
import com.hrznstudio.titanium.block.BlockBase;
import com.hrznstudio.titanium.recipe.generator.IJSONGenerator;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ResourceTypeBlock extends BlockBase implements IJsonFile, IJSONGenerator, IResourceHolder, IHasColor {

    private final ResourceMaterial resourceMaterial;
    private final IResourceType resourceType;
    private final IBlockResourceType blockResourceType;

    public ResourceTypeBlock(ResourceMaterial material, IResourceType type, IBlockResourceType blockType, ResourceTypeProperties<Properties> properties) {
        super(material.getMaterialType() + "_" + type.getName(), (properties == null ? ((ResourceTypeProperties<Properties>) ResourceTypeProperties.DEFAULTS.get(Block.class)).get() : properties.get()));
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
        return blockResourceType.generate();
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
    public IFactory<BlockItem> getItemBlockFactory() {
        return () -> (BlockItem) new BlockItem(this, new Item.Properties().group(this.getItemGroup())) {
            @Override
            public ITextComponent getDisplayName(ItemStack p_200295_1_) {
                return resourceType.getTextComponent(resourceMaterial.getTextComponent());
            }
        }.setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

    public enum BlockResourceType implements IBlockResourceType {
        METAL_BLOCK((material1, integer) -> material1.getColor(), () -> {
            JsonObject object = new JsonObject();
            object.addProperty("parent", "block/cube_all");
            JsonObject textures = new JsonObject();
            textures.addProperty("all", new ResourceLocation(Titanium.MODID, "blocks/resource/metal_block").toString());
            object.add("textures", textures);
            return object;
        }),
        ORE((material1, integer) -> integer == 0 ? material1.getColor() : 1, () -> {
            JsonObject object = new JsonObject();
            object.addProperty("parent", Titanium.MODID + ":block/ore");
            JsonObject textures = new JsonObject();
            textures.addProperty("ore", new ResourceLocation(Titanium.MODID, "blocks/resource/ore_overlay").toString());
            object.add("textures", textures);
            return object;
        }),
        NETHER_ORE((material1, integer) -> integer == 0 ? material1.getColor() : 1, () -> {
            JsonObject object = new JsonObject();
            object.addProperty("parent", Titanium.MODID + ":block/ore");
            JsonObject textures = new JsonObject();
            textures.addProperty("ore", new ResourceLocation(Titanium.MODID, "blocks/resource/ore_overlay").toString());
            textures.addProperty("particle", "blocks/netherrack");
            textures.addProperty("texture", "blocks/netherrack");
            object.add("textures", textures);
            return object;
        }),
        GEM_BLOCK((material1, integer) -> material1.getColor(), () -> {
            JsonObject object = new JsonObject();
            object.addProperty("parent", "block/cube_all");
            JsonObject textures = new JsonObject();
            textures.addProperty("all", new ResourceLocation(Titanium.MODID, "blocks/resource/gem_block").toString());
            object.add("textures", textures);
            return object;
        });

        private final BiFunction<ResourceMaterial, Integer, Integer> colorFunction;
        private final Supplier<JsonObject> jsonObjectSupplier;

        BlockResourceType(BiFunction<ResourceMaterial, Integer, Integer> colorFunction, Supplier<JsonObject> jsonObjectSupplier) {
            this.colorFunction = colorFunction;
            this.jsonObjectSupplier = jsonObjectSupplier;
        }

        @Override
        public int getColor(ResourceMaterial material, int tintIndex) {
            return colorFunction.apply(material, tintIndex);
        }

        @Override
        public JsonObject generate() {
            return jsonObjectSupplier.get();
        }
    }
}
