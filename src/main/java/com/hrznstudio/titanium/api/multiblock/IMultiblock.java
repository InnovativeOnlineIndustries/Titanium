package com.hrznstudio.titanium.api.multiblock;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;

public interface IMultiblock extends IForgeRegistryEntry<Block> {

    Predicate<IBlockState>[][][] getStructureBlocks();

    boolean isController(int x, int y, int z);

    boolean isStructureMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing playerFacing);

    void createStructureMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing playerFacing);

    void destroyMultiblock(World world, BlockPos pos, IBlockState state, EnumFacing facing);

}
