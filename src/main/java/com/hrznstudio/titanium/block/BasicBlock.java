/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IRecipeProvider;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.datagenerator.loot.block.BasicBlockLootTables;
import com.hrznstudio.titanium.datagenerator.loot.block.IBlockLootTableProvider;
import com.hrznstudio.titanium.module.api.IAlternativeEntries;
import com.hrznstudio.titanium.module.api.RegistryManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class BasicBlock extends Block implements IAlternativeEntries, IRecipeProvider, IBlockLootTableProvider {
    private ItemGroup itemGroup = ItemGroup.SEARCH;
    private BlockItem item;

    public BasicBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vector3d start, Vector3d end, VoxelShape shape) {
        BlockRayTraceResult bbResult = shape.rayTrace(start, end, pos);
        if (bbResult != null) {
            Vector3d hitVec = bbResult.getHitVec();
            Direction sideHit = bbResult.getFace();
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
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext) {
        if (hasCustomBoxes(state, world, pos)) {
            VoxelShape shape = VoxelShapes.empty();
            for (VoxelShape shape1 : getBoundingBoxes(state, world, pos)) {
                shape = VoxelShapes.combineAndSimplify(shape, shape1, IBooleanFunction.OR);
            }
            return shape;
        }
        return super.getCollisionShape(state, world, pos, selectionContext);
    }

    public IFactory<BlockItem> getItemBlockFactory() {
        return () -> (BlockItem) new BlockItem(this, new Item.Properties().group(this.itemGroup)).setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

    @Override
    public void addAlternatives(RegistryManager<?> registry) {
        registry.content(Item.class, item = getItemBlockFactory().create());
    }

    @Override
    @Nonnull
    public Item asItem() {
        return item;
    }

    public void setItem(BlockItem item) {
        this.item = item;
    }

    public List<VoxelShape> getBoundingBoxes(BlockState state, IBlockReader source, BlockPos pos) {
        return Collections.emptyList();
    }

    public boolean hasCustomBoxes(BlockState state, IBlockReader source, BlockPos pos) {
        return false;
    }

    @Nullable
    protected RayTraceResult rayTraceBoxesClosest(Vector3d start, Vector3d end, BlockPos pos, List<VoxelShape> boxes) {
        List<DistanceRayTraceResult> results = new ArrayList<>();
        for (VoxelShape box : boxes) {
            DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
            if (hit != null)
                results.add(hit);
        }
        RayTraceResult closestHit = null;
        double curClosest = Double.MAX_VALUE;
        for (DistanceRayTraceResult hit : results) {
            if (curClosest > hit.getDistance()) {
                closestHit = hit;
                curClosest = hit.getDistance();
            }
        }
        return closestHit;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    @Override
    public void registerRecipe(Consumer<IFinishedRecipe> consumer) {

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            InventoryHelper.dropItems(worldIn, pos, getDynamicDrops(state, worldIn, pos, newState, isMoving));
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    public NonNullList<ItemStack> getDynamicDrops(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity != null) {
            tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
                for (int i = 0; i < iItemHandler.getSlots(); i++) {
                    stacks.add(iItemHandler.getStackInSlot(i));
                }
            });
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
