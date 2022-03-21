/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IRecipeProvider;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import com.hrznstudio.titanium.datagenerator.loot.block.IBlockLootTableProvider;
import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BasicBlock extends Block implements IRecipeProvider, IBlockLootTableProvider {
    private CreativeModeTab itemGroup = CreativeModeTab.TAB_SEARCH;
    private final String name;

    public BasicBlock(String name, Properties properties) {
        super(properties);
        this.name = name;
    }

    @Nullable
    protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3 start, Vec3 end, VoxelShape shape) {
        BlockHitResult bbResult = shape.clip(start, end, pos);
        if (bbResult != null) {
            Vec3 hitVec = bbResult.getLocation();
            Direction sideHit = bbResult.getDirection();
            double dist = start.distanceTo(hitVec);
            return new DistanceRayTraceResult(hitVec, sideHit, pos, shape, dist);
        }
        return null;
    }

    // TODO: getBlockHardness is moved to BlockState, couldn't find an answer on how to adress this.
//    @Override
//    @SuppressWarnings("deprecation")
//    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
//        return 1.5F;
//    }

    //@Nullable
    //@Override
    //public RayTraceResult getRayTraceResult(BlockState state, World world, BlockPos pos, Vector3d start, Vector3d end, RayTraceResult original) {
    //    if (hasCustomBoxes(state, world, pos)) {
    //        return rayTraceBoxesClosest(start, end, pos, getBoundingBoxes(state, world, pos));
    //    }
    //    return super.getRayTraceResult(state, world, pos, start, end, original);
    //}

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
        if (hasCustomBoxes(state, world, pos)) {
            VoxelShape shape = Shapes.empty();
            for (VoxelShape shape1 : getBoundingBoxes(state, world, pos)) {
                shape = Shapes.join(shape, shape1, BooleanOp.OR);
            }
            return shape;
        }
        return super.getCollisionShape(state, world, pos, selectionContext);
    }

    public Supplier<Item> getItemBlockFactory() {
        return () -> (Item) new BlockItem(this, new Item.Properties().tab(this.itemGroup));
    }

    public List<VoxelShape> getBoundingBoxes(BlockState state, BlockGetter source, BlockPos pos) {
        return Collections.emptyList();
    }

    public boolean hasCustomBoxes(BlockState state, BlockGetter source, BlockPos pos) {
        return false;
    }

    @Nullable
    protected HitResult rayTraceBoxesClosest(Vec3 start, Vec3 end, BlockPos pos, List<VoxelShape> boxes) {
        List<DistanceRayTraceResult> results = new ArrayList<>();
        for (VoxelShape box : boxes) {
            DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
            if (hit != null)
                results.add(hit);
        }
        HitResult closestHit = null;
        double curClosest = Double.MAX_VALUE;
        for (DistanceRayTraceResult hit : results) {
            if (curClosest > hit.getDistance()) {
                closestHit = hit;
                curClosest = hit.getDistance();
            }
        }
        return closestHit;
    }

    public CreativeModeTab getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(CreativeModeTab itemGroup) {
        this.itemGroup = itemGroup;
    }

    @Override
    public void registerRecipe(Consumer<FinishedRecipe> consumer) {

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Containers.dropContents(worldIn, pos, getDynamicDrops(state, worldIn, pos, newState, isMoving));
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof ActiveTile && ((ActiveTile<?>) tileentity).getMultiInventoryComponent() != null) {
            for (InventoryComponent<?> inventoryHandler : ((ActiveTile<?>) tileentity).getMultiInventoryComponent().getInventoryHandlers()) {
                for (int i = 0; i < inventoryHandler.getSlots(); i++) {
                    stacks.add(inventoryHandler.getStackInSlot(i));
                }
            }
        }
        return stacks;
    }

    public boolean hasIndividualRenderVoxelShape() {
        return false;
    }

    public LootTable.Builder getLootTable(@Nonnull BasicBlockLootTables blockLootTables) {
        return blockLootTables.droppingSelf(this);
    }

}
