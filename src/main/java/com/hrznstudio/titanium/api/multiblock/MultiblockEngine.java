package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public abstract class MultiblockEngine implements IMultiblockHandler {

    private final ResourceLocation id;

    @Nullable
    private Template template;

    private BlockPos masterPos;
    private BlockPos triggerPos;

    @Nullable
    private List<BlockState> blockStates;
    private BlockState triggerState;

    public MultiblockEngine(ResourceLocation id, BlockPos masterPos, BlockPos triggerPos) {
        this.id = id;
        this.masterPos = masterPos;
        this.triggerPos = triggerPos;
    }

    @Override
    public boolean isBlockTrigger(BlockState state) {
        getTemplate();
        return state.getBlock().equals(triggerState.getBlock());
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

    public ResourceLocation getId() {
        return id;
    }

    @Nullable
    private Template getTemplate() {
        if (template == null) {
            try {
                template = StaticTemplateHandler.loadStaticTemplate(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return template;
    }

}
