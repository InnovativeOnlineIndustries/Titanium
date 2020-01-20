package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.api.multiblock.IMultiblockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MultiblockTestHandler implements IMultiblockHandler {
    

    @Override
    public boolean isBlockTrigger(BlockState state) {
        return false;
    }

    @Override
    public boolean createStructure(World world, BlockPos controllerPos, Direction direction, PlayerEntity playerEntity) {
        return false;
    }

    @Override
    public void breakStructure(World world, BlockPos controllerPos, Direction direction, Block block, PlayerEntity playerEntity) {

    }

    @Override
    public boolean canRenderFormedMultiblock() {
        return false;
    }

    @Override
    public void renderFormedMultiblock() {

    }

    @Override
    public IMultiblockHandler setRegistryName(ResourceLocation name) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }

    @Override
    public Class<IMultiblockHandler> getRegistryType() {
        return null;
    }
}
