/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.registries.RegistryObject;

public class TitaniumFluidInstance extends net.minecraftforge.registries.ForgeRegistryEntry<TitaniumFluidInstance> {

    private RegistryObject<Fluid> flowingFluid;
    private RegistryObject<Fluid> sourceFluid;
    private RegistryObject<Item> bucketFluid;
    private RegistryObject<Block> blockFluid;
    private final String fluid;

    public TitaniumFluidInstance(DeferredRegistryHelper helper, String fluid, FluidAttributes.Builder attributes, CreativeModeTab group) {
        this.fluid = fluid;
        this.sourceFluid = helper.registerGeneric(Fluid.class, fluid, () -> new TitaniumFluid.Source(attributes, this));
        this.flowingFluid = helper.registerGeneric(Fluid.class, fluid + "_flowing", () ->  new TitaniumFluid.Flowing(attributes, this));
        this.bucketFluid = helper.registerGeneric(Item.class, fluid + "_bucket", () -> new BucketItem(this.sourceFluid, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(group)));
        this.blockFluid = helper.registerGeneric(Block.class, fluid, () -> new LiquidBlock(() -> (FlowingFluid) sourceFluid.get(), Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
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
