/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class TitaniumFluidInstance {

    private RegistryObject<FluidType> fluidType;
    private RegistryObject<Fluid> flowingFluid;
    private RegistryObject<Fluid> sourceFluid;
    private RegistryObject<Item> bucketFluid;
    private RegistryObject<Block> blockFluid;
    private final String fluid;

    public TitaniumFluidInstance(DeferredRegistryHelper helper, String fluid, FluidType.Properties fluidTypeProperties, IClientFluidTypeExtensions renderProperties, CreativeModeTab group) {
        this.fluid = fluid;
        this.sourceFluid = helper.registerGeneric(ForgeRegistries.FLUIDS.getRegistryKey(), fluid, () -> new TitaniumFluid.Source(this));
        this.flowingFluid = helper.registerGeneric(ForgeRegistries.FLUIDS.getRegistryKey(), fluid + "_flowing", () -> new TitaniumFluid.Flowing(this));
        this.fluidType = helper.registerGeneric(ForgeRegistries.Keys.FLUID_TYPES, fluid, () -> new FluidType(fluidTypeProperties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(renderProperties);
            }
        });
        this.bucketFluid = helper.registerGeneric(ForgeRegistries.ITEMS.getRegistryKey(), fluid + "_bucket", () -> new BucketItem(this.sourceFluid, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(group)));
        this.blockFluid = helper.registerGeneric(ForgeRegistries.BLOCKS.getRegistryKey(), fluid, () -> new LiquidBlock(() -> (FlowingFluid) sourceFluid.get(), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noLootTable()));
    }

    public RegistryObject<FluidType> getFluidType() {
        return fluidType;
    }
    public RegistryObject<Fluid>  getFlowingFluid() {
        return flowingFluid;
    }

    public RegistryObject<Fluid>  getSourceFluid() {
        return sourceFluid;
    }

    public RegistryObject<Item> getBucketFluid() {
        return bucketFluid;
    }

    public RegistryObject<Block> getBlockFluid() {
        return blockFluid;
    }

    public String getFluid() {
        return fluid;
    }
}
