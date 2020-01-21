package com.hrznstudio.titanium.multiblock.tile;

import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MachineControllerTile<T extends MachineControllerTile<T>> extends ActiveTile<T> implements IMultiblockComponent {

    private BlockState originalState;
    private boolean isFormed = false;
    private List<Pair<BlockPos, BlockState>> children = new ArrayList<>();

    public MachineControllerTile(BasicTileBlock<T> base, BlockState originalState) {
        super(base);
        this.originalState = originalState;
    }

    public void addChild(MachineFillerTile child) {
        children.add(Pair.of(child.getPos(), child.getOriginalState()));
    }

    public void onBreak() {
        children.forEach(pair -> {
            world.setBlockState(pair.getKey(), pair.getValue());
        });
        world.setBlockState(getPos(), originalState);
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, facing, hitX, hitY, hitZ) == ActionResultType.FAIL) {
            if (!player.isSneaking()) {
                openGui(player);
                return ActionResultType.SUCCESS;
            }
            return ActionResultType.FAIL;
        } else {
            return ActionResultType.SUCCESS;
        }
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    @Nonnull
    public <U> LazyOptional<U> getMimicCapbility(@Nonnull Capability<U> cap, @Nullable Direction side, BlockPos mimicPos) {
        return getCapability(cap, side);
    }

    @Nonnull
    @Override
    public T getSelf() {
        return null;
    }

    public boolean isFormed() {
        return this.isFormed;
    }

    public void setFormed(boolean formed) {
        this.isFormed = formed;
    }
}
