/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import com.hrznstudio.titanium.tab.TitaniumTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TitaniumFluidInstance {
    private static final List<TitaniumFluidInstance> INSTANCES = new CopyOnWriteArrayList<>();

    private DeferredHolder<FluidType, FluidType> fluidType;
    private DeferredHolder<Fluid, Fluid> flowingFluid;
    private DeferredHolder<Fluid, Fluid> sourceFluid;
    private DeferredHolder<Item, Item> bucketFluid;
    private DeferredHolder<Block, Block> blockFluid;
    private final String fluid;
    private final IClientFluidTypeExtensions renderProperties;

    public TitaniumFluidInstance(DeferredRegistryHelper helper, String fluid, FluidType.Properties fluidTypeProperties, IClientFluidTypeExtensions renderProperties, @Nullable TitaniumTab group) {
        this.fluid = fluid;
        this.renderProperties = renderProperties;
        INSTANCES.add(this);
        this.sourceFluid = helper.registerGeneric(Registries.FLUID, fluid, () -> new TitaniumFluid.Source(this));
        this.flowingFluid = helper.registerGeneric(Registries.FLUID, fluid + "_flowing", () -> new TitaniumFluid.Flowing(this));
        this.fluidType = helper.registerGeneric(NeoForgeRegistries.Keys.FLUID_TYPES, fluid, () -> new FluidType(fluidTypeProperties) {
        });
        this.bucketFluid = helper.registerGeneric(Registries.ITEM, fluid + "_bucket", () -> {
            var item = new BucketItem(this.sourceFluid.get(), DeferredRegistryHelper.applyItemRegistrationId(new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1));
            if (group != null) group.getTabList().add(item);
            return item;
        });
        this.blockFluid = helper.registerGeneric(Registries.BLOCK, fluid, () -> new LiquidBlock((FlowingFluid) sourceFluid.get(), DeferredRegistryHelper.applyBlockRegistrationId(Block.Properties.of()).mapColor(MapColor.WATER).replaceable().noCollision().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)));
    }

    public DeferredHolder<FluidType, FluidType> getFluidType() {
        return fluidType;
    }
    public DeferredHolder<Fluid, Fluid>  getFlowingFluid() {
        return flowingFluid;
    }

    public DeferredHolder<Fluid, Fluid>  getSourceFluid() {
        return sourceFluid;
    }

    public DeferredHolder<Item, Item> getBucketFluid() {
        return bucketFluid;
    }

    public DeferredHolder<Block, Block> getBlockFluid() {
        return blockFluid;
    }

    public String getFluid() {
        return fluid;
    }

    public static List<TitaniumFluidInstance> getInstances() {
        return List.copyOf(INSTANCES);
    }

    public IClientFluidTypeExtensions getRenderProperties() {
        return renderProperties;
    }

    @Nullable
    public TitaniumFluidRenderProperties getFluidRenderProperties() {
        return renderProperties instanceof TitaniumFluidRenderProperties properties ? properties : null;
    }
}
