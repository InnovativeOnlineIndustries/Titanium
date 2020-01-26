/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.multiblock.IFormationItem;
import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.api.multiblock.MultiblockTemplate;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MachineControllerTile<T extends MachineControllerTile<T>> extends ActiveTile<T> implements IMultiblockComponent {

    @Save
    private BlockPos masterPos = BlockPos.ZERO;
    @Save
    private BlockPos posInMultiblock = BlockPos.ZERO;
    @Save
    private BlockPos posRSBlock = BlockPos.ZERO;
    @Save
    private boolean hasRedStoneSignal = false;
    @Save
    private boolean isFormed = false;
    @Save
    private Item formationTool;
    @Save
    private BlockState originalState;

    private MultiblockTemplate multiblockTemplate;

    private List<Pair<BlockPos, BlockState>> children = new ArrayList<>();

    public MachineControllerTile(BasicTileBlock<T> base) {
        this(base, null);
    }

    public MachineControllerTile(BasicTileBlock<T> base, @Nullable Item formationTool) {
        super(base);
        this.formationTool = formationTool;
    }

    public void addChild(MachineFillerTile child) {
        children.add(Pair.of(child.getPos(), child.getOriginalState()));
    }

    public void onBreak() {
        if(isFormed() && !world.isRemote) {
            children.forEach(pair -> {
                world.setBlockState(pair.getKey(), pair.getValue());
            });
            world.setBlockState(getPos(), originalState);
        }
    }

    @Override
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(player, hand, facing, hitX, hitY, hitZ) == ActionResultType.PASS) {
            ItemStack stack = player.getHeldItem(hand);
            Item item = stack.getItem();
            boolean isItemFormationTool = stack.isItemEqual(formationTool.getDefaultInstance());
            if (!player.isSneaking() && isFormed) {
                openGui(player);
                return ActionResultType.SUCCESS;
            } else if(isItemFormationTool){
                if(item instanceof IFormationItem){
                    if(((IFormationItem) item).isConsumable()){
                        int cost = ((IFormationItem) item).formationCost();
                        setFormed(true);
                        if(cost != 0) {
                            stack.shrink(((IFormationItem) item).formationCost());
                            return ActionResultType.SUCCESS;
                        }else{
                            stack.shrink(1);
                            return ActionResultType.SUCCESS;
                        }
                    }else{
                        setFormed(true);
                        return ActionResultType.SUCCESS;
                    }
                }
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

    public void setFormed(boolean isFormed) {
        this.isFormed = isFormed;
    }

    @Override
    public BlockPos getPosition() {
        return null;
    }

    @Override
    public BlockPos getMasterPosition() {
        return masterPos;
    }

    @Override
    public Direction getFacing() {
        return null;
    }

    @Override
    public void setFacing(Direction facing) {

    }

    public boolean hasRedStoneSignal() {
        return hasRedStoneSignal;
    }

    public void updateMasterBlock(BlockState state, boolean blockUpdate) {
        if(blockUpdate) {
            markForUpdate();
        }else{
            markDirty();
        }
    }

    public boolean targetFormationSide(ItemStack stack, Direction direction, PlayerEntity player, Vec3d vec3d, boolean consumable){
       if(!world.isRemote) {
           if(formationTool != null) {

               if (consumable) {

               } else {

               }
           } else {

           }
       }
        return false;
    }

}
