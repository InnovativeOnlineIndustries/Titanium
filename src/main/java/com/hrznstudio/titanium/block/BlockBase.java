/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.module.api.IAlternativeEntries;
import com.hrznstudio.titanium.module.api.RegistryManager;
import com.hrznstudio.titanium.recipe.LootPoolBlock;
import com.hrznstudio.titanium.recipe.generator.JsonDataGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.*;

public abstract class BlockBase extends Block implements IAlternativeEntries {

    public static JsonDataGenerator BLOCK_LOOT = new JsonDataGenerator(JsonDataGenerator.DataTypes.LOOT_BLOCKS, Titanium.MODID);

    private ItemGroup itemGroup = ItemGroup.SEARCH;
    private BlockItem item;

    public BlockBase(String name, Properties properties) {
        super(properties);
        setRegistryName(name);
        BLOCK_LOOT.addRecipe(new LootPoolBlock(this.getRegistryName(), getStaticDrops()));
    }

    @Nullable
    protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3d start, Vec3d end, VoxelShape shape) {
        RayTraceResult bbResult = shape.rayTrace(start, end, pos);
        if (bbResult != null) {
            Vec3d hitVec = bbResult.getHitVec();
            Direction sideHit = ((BlockRayTraceResult) bbResult).getFace();
            double dist = start.distanceTo(hitVec);
            return new DistanceRayTraceResult(hitVec, sideHit, pos, shape, dist);
        }
        return null;
    }

    @Override
    public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
        return 1.5F;
    }

    @Override
    public ResourceLocation getLootTable() {
        return new ResourceLocation(getRegistryName().getNamespace(), "blocks/" + getRegistryName().getNamespace() + "_" + getRegistryName().getPath());
    }

    @Nullable
    @Override
    public RayTraceResult getRayTraceResult(BlockState state, World world, BlockPos pos, Vec3d start, Vec3d end, RayTraceResult original) {
        if (hasCustomBoxes(state, world, pos)) {
            return rayTraceBoxesClosest(start, end, pos, getBoundingBoxes(state, world, pos));
        }
        return super.getRayTraceResult(state, world, pos, start, end, original);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
        if (hasCustomBoxes(state, worldIn, pos)) {
            VoxelShape shape = null;
            for (VoxelShape shape1 : getBoundingBoxes(state, worldIn, pos)) {
                if (shape == null) {
                    shape = shape1;
                } else {
                    shape = VoxelShapes.combineAndSimplify(shape, shape1, IBooleanFunction.OR);
                }
            }
            return shape;
        }
        return super.getCollisionShape(state, worldIn, pos, selectionContext);
    }

    public IFactory<BlockItem> getItemBlockFactory() {
        return () -> (BlockItem) new BlockItem(this, new Item.Properties().group(this.itemGroup)).setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

    @Override
    public void addAlternatives(RegistryManager registry) {
        registry.content(Item.class, item = getItemBlockFactory().create());
    }

    @Override
    public Item asItem() {
        return item;
    }

    public List<VoxelShape> getBoundingBoxes(BlockState state, IBlockReader source, BlockPos pos) {
        return Collections.emptyList();
    }

    public boolean hasCustomBoxes(BlockState state, IBlockReader source, BlockPos pos) {
        return false;
    }

    @Nullable
    protected RayTraceResult rayTraceBoxesClosest(Vec3d start, Vec3d end, BlockPos pos, List<VoxelShape> boxes) {
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

    public void setItemGroup(ItemGroup itemGroup) {
        this.itemGroup = itemGroup;
    }

    public ItemGroup getItemGroup() {
        return itemGroup;
    }

    public List<Pool> getStaticDrops() {
        List<Pool> pools = Collections.emptyList();
        pools.add(new Pool(1, new Pool.Entry[]{Pool.Entry.of(this)}, new Pool.Condition[]{Pool.Condition.of(new ResourceLocation("minecraft", "survives_explosion"))}));
        return pools;
    }

    @Override
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

    public static class Pool {

        private int rolls;
        private Entry[] entries;
        private Condition[] conditions;

        public Pool(int rolls, Entry[] entries, Condition[] conditions) {
            this.rolls = rolls;
            this.entries = entries;
            this.conditions = conditions;
        }

        public static class Entry {

            private final String type = "minecraft:item";
            private String name;

            private Entry(String name) {
                this.name = name;
            }

            public static Entry of(Block block) {
                return new Entry(block.getRegistryName().toString());
            }
        }

        public static class Condition {

            private String condition;

            private Condition(String string) {
                this.condition = string;
            }

            public static Condition of(ResourceLocation location) {
                return new Condition(location.toString());
            }

        }
    }

}