/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IMachine;
import com.hrznstudio.titanium.api.augment.IAugmentType;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.client.screen.addon.AssetScreenAddon;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.item.AugmentWrapper;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MachineTile<T extends MachineTile<T>> extends PoweredTile<T> implements IMachine {
    @Save
    private SidedInventoryComponent<T> augmentInventory;

    public MachineTile(BasicTileBlock<T> basicTileBlock, BlockPos pos, BlockState state) {
        super(basicTileBlock, pos, state);
        addInventory(this.augmentInventory = (SidedInventoryComponent<T>) getAugmentFactory()
            .create()
            .setComponentHarness(this.getSelf())
            .setInputFilter((stack, integer) -> AugmentWrapper.isAugment(stack) && canAcceptAugment(stack)));
        addGuiAddonFactory(getAugmentBackground());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            augmentInventory.getFacingModes().put(value, IFacingComponent.FaceMode.NONE);
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public boolean canAcceptAugment(ItemStack augment) {
        return AugmentWrapper.isAugment(augment);
    }

    @Override
    public List<ItemStack> getInstalledAugments() {
        return getItemStackAugments().stream().filter(AugmentWrapper::isAugment).collect(Collectors.toList());
    }

    @Override
    public List<ItemStack> getInstalledAugments(IAugmentType filter) {
        return getItemStackAugments().stream().filter(AugmentWrapper::isAugment).filter(stack -> AugmentWrapper.hasType(stack, filter)).collect(Collectors.toList());
    }

    @Override
    public boolean hasAugmentInstalled(IAugmentType augmentType) {
        return getInstalledAugments(augmentType).size() > 0;
    }

    public IFactory<InventoryComponent<T>> getAugmentFactory() {
        return () -> new SidedInventoryComponent<T>("augments", 180, 11, 4, 0)
            .disableFacingAddon()
            .setColor(DyeColor.PURPLE)
            .setSlotLimit(1)
                .setRange(1, 4);
    }

    public IFactory<? extends IScreenAddon> getAugmentBackground() {
        return () -> new AssetScreenAddon(AssetTypes.AUGMENT_BACKGROUND, 175, 4, true);
    }

    private List<ItemStack> getItemStackAugments() {
        List<ItemStack> augments = new ArrayList<>();
        for (int i = 0; i < augmentInventory.getSlots(); i++) {
            augments.add(augmentInventory.getStackInSlot(i));
        }
        return augments;
    }

    @Override
    public InteractionResult onActivated(Player playerIn, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ) == InteractionResult.SUCCESS) {
            return InteractionResult.SUCCESS;
        }
        openGui(playerIn);
        return InteractionResult.SUCCESS;
    }

}
