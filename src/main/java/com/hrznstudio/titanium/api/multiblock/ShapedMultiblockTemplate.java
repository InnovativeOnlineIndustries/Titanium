/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock;

import com.google.common.collect.ImmutableList;
import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.util.matcher.IMatch;
import com.hrznstudio.titanium.util.matcher.Matcher;
import com.hrznstudio.titanium.util.BlockPosUtil;
import com.hrznstudio.titanium.util.FacingUtil;
import com.hrznstudio.titanium.util.StaticTemplateUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

/**
 * Big shout-outs and credits to Immersive Engineering for providing most of the inspiration and example implementation code for us to figure out this implementation.
 *
 * IE is licensed under "Blu's License of Common Sense" as seen here:
 * https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE
 *
 * You should also go check out their github repo:
 * https://github.com/BluSunrize/ImmersiveEngineering
 */
public abstract class ShapedMultiblockTemplate implements IMultiblock {

    private final ResourceLocation id;
    private final List<IMatch> additionalPredicates;
    @Nullable
    private Template template;
    private BlockPos masterPos;
    private BlockPos triggerPos;
    @Nullable
    private List<BlockState> blockStates;
    private BlockState triggerState;

    public ShapedMultiblockTemplate(ResourceLocation id, BlockPos masterPos, BlockPos triggerPos, List<IMatch> additionalPredicates) {
        this.id = id;
        this.masterPos = masterPos;
        this.triggerPos = triggerPos;
        this.additionalPredicates = additionalPredicates;
    }

    public ShapedMultiblockTemplate(ResourceLocation id, BlockPos masterPos, BlockPos triggerPos) {
        this.id = id;
        this.masterPos = masterPos;
        this.triggerPos = triggerPos;
        this.additionalPredicates = ImmutableList.of();
    }

    @Override
    public boolean isBlockTrigger(BlockState state) {
        getTemplate();
        return state.getBlock().equals(triggerState.getBlock());
    }

    @Override
    public boolean createStructure(World world, BlockPos controllerPos, Direction direction, PlayerEntity playerEntity) {

        if (direction.getAxis() == Direction.Axis.Y) {
            direction = Direction.fromAngle(playerEntity.rotationYaw);
        }

        Rotation rotation = FacingUtil.getRotationBetweenFacings(Direction.NORTH, direction.getOpposite());
        if (rotation == null) {
            return false;
        }

        Template template = getTemplate();
        List<Mirror> mirrorStates;
        if (canBeMirrored()) {
            mirrorStates = ImmutableList.of(Mirror.NONE, Mirror.FRONT_BACK);
        } else {
            mirrorStates = ImmutableList.of(Mirror.NONE);
        }

        mirrorLoop:
        for (Mirror mirror : mirrorStates) {
            PlacementSettings placeSet = new PlacementSettings().setMirror(mirror).setRotation(rotation);
            BlockPos origin = controllerPos.subtract(Template.transformedBlockPos(placeSet, triggerPos));
            for (Template.BlockInfo info : template.blocks.get(0)) {
                BlockPos realRelPos = Template.transformedBlockPos(placeSet, info.pos);
                BlockPos here = origin.add(realRelPos);
                BlockState expected = info.state.mirror(mirror).rotate(rotation);
                BlockState inWorld = world.getBlockState(here);

                if (!Matcher.matches(expected, inWorld, world, here, additionalPredicates).isAllowed()) {
                    continue mirrorLoop;
                }
            }
            formStructure(world, origin, rotation, mirror, direction);
            return true;
        }
        return false;
    }

    @Override
    public void formStructure(World world, BlockPos originPos, Rotation rotation, Mirror mirror, Direction direction) {
        BlockPos truePos = BlockPosUtil.withSettingsAndOffset(originPos, masterPos, mirror, rotation);
        for (Template.BlockInfo blockInfo : template.blocks.get(0)) {
            BlockPos actualPos = BlockPosUtil.withSettingsAndOffset(truePos, blockInfo.pos, mirror, rotation);
        }
    }

    @Override
    public boolean canRenderFormedMultiblock() {
        return false;
    }

    @Override
    public void renderFormedMultiblock() {
    }

    public ResourceLocation getId() {
        return id;
    }

    @Nullable
    private Template getTemplate() {
        if (template == null) {
            try {
                template = StaticTemplateUtil.loadStaticTemplate(id);
            } catch (IOException e) {
                Titanium.LOGGER.error(e);
            }
        }
        return template;
    }

    private boolean canBeMirrored() {
        return true;
    }

    public List<Template.BlockInfo> getStructure()
    {
        return getTemplate().blocks.get(0);
    }

    public Vec3i getSize()
    {
        return getTemplate().getSize();
    }
}