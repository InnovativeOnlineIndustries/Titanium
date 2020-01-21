package com.hrznstudio.titanium.api.multiblock;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.multiblock.tile.MachineControllerTile;
import com.hrznstudio.titanium.multiblock.tile.MachineFillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;

import java.util.function.Supplier;

public class TitaniumMultiblockTemplate extends MultiblockTemplate {

    private final Supplier<BlockState> blockStateSupplier;

    public TitaniumMultiblockTemplate(ResourceLocation id, BlockPos masterPos, BlockPos triggerPos, Supplier<BlockState> blockStateSupplier) {
        super(id, masterPos, triggerPos);
        this.blockStateSupplier = blockStateSupplier;
    }

    @Override
    protected void prepareBlockForDisassembly(World world, BlockPos originPos) {
        TileEntity tileEntity = world.getTileEntity(originPos);
        if (isTileEntityMultiblock(tileEntity) && tileEntity instanceof IMultiblockComponent) {
            IMultiblockComponent component = (IMultiblockComponent) tileEntity;
            if (component.isFormed()) {
                component.setFormed(false);
            }
        } else {
            Titanium.LOGGER.error("Tile at BlockPos: " + originPos + ", should have been a IMultiblockComponent Tile, It wasn't... FIX YOH SHIT!");
        }
    }

    @Override
    protected void replaceStructureBlock(Template.BlockInfo info, World world, BlockPos originPos, boolean mirrored, Direction clickDirection, Vec3i offsetFromMaster) {
        BlockState baseState = blockStateSupplier.get();
        if (offsetFromMaster != Vec3i.NULL_VECTOR) {
          //TODO: Assign Slave to BlockState
        }
        world.setBlockState(originPos, baseState);
        TileEntity tileEntity = world.getTileEntity(originPos);
        if (tileEntity instanceof IMultiblockComponent) {
            IMultiblockComponent component = (IMultiblockComponent) tileEntity;
            component.setFormed(true);
        } else {
            Titanium.LOGGER.error("Tile at BlockPos: " + originPos + ", should have been a IMultiblockComponent Tile, It wasn't... FIX YOH SHIT!");
        }
        world.addBlockEvent(originPos, world.getBlockState(originPos).getBlock(), 255, 0);
    }

    private boolean isTileEntityMultiblock(TileEntity tileEntity) {
        return tileEntity instanceof MachineControllerTile || tileEntity instanceof MachineFillerTile;
    }

    public Supplier<BlockState> getBlockStateSupplier() {
        return blockStateSupplier;
    }
}