package com.hrznstudio.titanium.event.custom;

import com.hrznstudio.titanium.api.multiblock.IMultiblock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public class MultiblockFormationEvent extends PlayerEvent {

    private final IMultiblock multiblock;
    private final BlockPos controller;
    private final ItemStack formationTool;

    public MultiblockFormationEvent(PlayerEntity player, IMultiblock multiblock, BlockPos controller, ItemStack formationTool) {
        super(player);
        this.multiblock = multiblock;
        this.controller = controller;
        this.formationTool = formationTool;
    }

    public IMultiblock getMultiblock() {
        return multiblock;
    }

    public BlockPos getController() {
        return controller;
    }

    public ItemStack getFormationTool() {
        return formationTool;
    }

    /**
     * This event is fired after the initial @IShapedMultiblock#isBlockTrigger check.
     * This event is the targeted event for cancellation by other mods.
     */
    @Cancelable
    public static class Pre extends MultiblockFormationEvent {
        public Pre(PlayerEntity player, IMultiblock multiblock, BlockPos controller, ItemStack formationTool) {
            super(player, multiblock, controller, formationTool);
        }
    }


    public static class Post extends MultiblockFormationEvent {
        public Post(PlayerEntity player, IMultiblock multiblock, BlockPos controller, ItemStack formationTool) {
            super(player, multiblock, controller, formationTool);
        }
    }
}
