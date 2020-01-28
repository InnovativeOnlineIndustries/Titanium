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
import com.hrznstudio.titanium.api.multiblock.ShapedMultiblockTemplate;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.Template;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.hrznstudio.titanium.util.TitaniumTags.FORMATION_TOOL;

public class MultiblockControllerTile<T extends MultiblockControllerTile<T>> extends ActiveTile<T> implements IMultiblockComponent {

    @Save
    private BlockPos masterPos = BlockPos.ZERO;
    @Save
    private BlockPos posRSBlock = BlockPos.ZERO;
    @Save
    private boolean hasRedStoneSignal = false;
    @Save
    private boolean isFormed = false;

    private Predicate<ItemStack> formationTool;

    private BlockState originalState;

    private ShapedMultiblockTemplate multiblockTemplate;

    private List<Pair<BlockPos, BlockState>> children = new ArrayList<>();

    public MultiblockControllerTile(ShapedMultiblockTemplate multiblockTemplate, BasicTileBlock<T> base) {
        super(base);
        this.multiblockTemplate = multiblockTemplate;
        this.formationTool = itemStack -> (itemStack.getItem().isIn(FORMATION_TOOL));
    }

    public MultiblockControllerTile(ShapedMultiblockTemplate multiblockTemplate, BasicTileBlock<T> base, Predicate<ItemStack> formationTool) {
        super(base);
        this.multiblockTemplate = multiblockTemplate;
        this.formationTool = formationTool;
    }

    public void addChild(MultiblockFillerTile child) {
        children.add(Pair.of(child.getPos(), child.getOriginalState()));
    }

    public void setOriginalState(MultiblockFillerTile child) {
        this.originalState = this.getBlockState();
        child.originalState = child.getBlockState();
    }

    public void onBreak() {
        if (isFormed() && !world.isRemote) {
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
            if (!player.isSneaking() && isFormed) {
                openGui(player);
                return ActionResultType.SUCCESS;
            } else if (formationTool.test(stack)) {
                Item item = stack.getItem();
                if (multiblockTemplate.createStructure(world, pos, facing, player)) {
                    formationHandler();
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
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
        if (blockUpdate) {
            markForUpdate();
        } else {
            markDirty();
        }
    }

    public void formationHandler() {
        for (Template.BlockInfo block : multiblockTemplate.getStructure()) {
            TileEntity target = world.getTileEntity(pos.add(block.pos.getX(), block.pos.getY(), block.pos.getZ()));
            if (target.getBlockState().equals(block.state)) {
                if (target instanceof MultiblockFillerTile) {
                    setOriginalState((MultiblockFillerTile) target);
                    addChild((MultiblockFillerTile) target);
                    setFormed(true);
                    ((MultiblockFillerTile) target).setFormed(true);
                    ((MultiblockFillerTile) target).setMasterPosition(this.getPos());
                    ((MultiblockFillerTile) target).setMultiblockTemplate(multiblockTemplate);
                }
            }
        }
    }
}
