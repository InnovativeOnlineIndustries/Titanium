/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import com.hrznstudio.titanium.module.api.IAlternativeEntries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidAttributes;

public class TitaniumFluidInstance extends net.minecraftforge.registries.ForgeRegistryEntry<TitaniumFluidInstance> implements IAlternativeEntries {

    private TitaniumFluid flowingFluid;
    private TitaniumFluid sourceFluid;
    private Item bucketFluid;
    private Block blockFluid;
    private final String fluid;

    public TitaniumFluidInstance(String modid, String fluid, FluidAttributes.Builder attributes, boolean hasBucket, CreativeModeTab group) {
        this.fluid = fluid;
        this.sourceFluid = (TitaniumFluid) new TitaniumFluid.Source(attributes);
        this.flowingFluid = (TitaniumFluid) new TitaniumFluid.Flowing(attributes);
        this.sourceFluid = this.sourceFluid.setSourceFluid(sourceFluid).setFlowingFluid(flowingFluid);
        this.flowingFluid = this.flowingFluid.setSourceFluid(sourceFluid).setFlowingFluid(flowingFluid);
        if (hasBucket)
            this.bucketFluid = new BucketItem(this.sourceFluid, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(group));
        this.blockFluid = new LiquidBlock(sourceFluid, Block.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()) {
        };
        this.sourceFluid.setBlockFluid(blockFluid).setBucketFluid(bucketFluid);
        this.flowingFluid.setBlockFluid(blockFluid).setBucketFluid(bucketFluid);
    }

    @Override
    public void addAlternatives(DeferredRegistryHelper registry) {
        registry.register(Fluid.class, fluid + "_fluid", () -> flowingFluid);
        registry.register(Fluid.class, fluid, () -> sourceFluid);
        registry.register(Block.class, fluid, () -> blockFluid);
        if (bucketFluid != null) registry.register(Item.class, fluid + "_bucket", () -> bucketFluid);
    }

    public TitaniumFluid getFlowingFluid() {
        return flowingFluid;
    }

    public TitaniumFluid getSourceFluid() {
        return sourceFluid;
    }

    public Item getBucketFluid() {
        return bucketFluid;
    }

    public Block getBlockFluid() {
        return blockFluid;
    }

    public void setBucketFluid(Item bucketFluid) {
        this.bucketFluid = bucketFluid;
        this.sourceFluid.setBucketFluid(bucketFluid);
        this.flowingFluid.setBucketFluid(bucketFluid);
    }
}
