package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.api.multiblock.IShapedMultiblock;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class MultiblockTest implements IShapedMultiblock {



    @Override
    public BlockState[][][] getMultiblock() {
        BlockState[][][] structure = new BlockState[3][3][3];
        for (int y = -1; y < 1; y++) {
            for (int x = -1; x < 1; x++) {
                for (int z = -1; z < 1; z++) {
                    if (y == 0 && x == 0 && z == 0) {
                        structure[x][y][z] = ForgeRegistries.FLUIDS.getValue(new ResourceLocation("minecraft:lava")).getStateContainer().getBaseState().getBlockState();
                    } else {
                        structure[x][y][z] = Blocks.COBBLESTONE.getDefaultState();
                    }
                }
            }
        }BlockPos
        return structure;
    }

    @Override
    public List<VoxelShape> getComponentMultiblockShapes() {
        List<VoxelShape> shapes = new ArrayList<>();
        shapes.add(VoxelShapes.create(0, 0, 0, 3, 3, 3).withOffset(0, 1, 0));
        return shapes;
    }

    @Override
    public boolean isBlockTrigger(BlockState state) {
        return state.equals(Blocks.COBBLESTONE.getDefaultState());
    }

    @Override
    public boolean createStructure(World world, BlockPos controllerPos, Direction direction, PlayerEntity playerEntity) {
        BlockState[][][] structureStates = getMultiblock();
        if (direction.getAxis().isVertical()) {
            direction = Direction.fromAngle(playerEntity.rotationYaw);
        } else {
            direction = direction.getOpposite();
        }

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    
                }
            }
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderFormedMultiblock() {
        return true;
    }

    static ItemStack renderStack = ItemStack.EMPTY;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderFormedMultiblock() {
        if (renderStack.isEmpty()) {
            renderStack = new ItemStack(Items.FURNACE);
        }

    }
}
