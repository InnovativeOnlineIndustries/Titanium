/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.IMachine;
import com.hrznstudio.titanium.api.augment.IAugment;
import com.hrznstudio.titanium.api.augment.IAugmentType;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.inventory.SidedInvHandler;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.client.gui.addon.AssetGuiAddon;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileMachine extends TilePowered implements IMachine {

    @Save
    private SidedInvHandler augmentInventory;

    public TileMachine(BlockTileBase blockTileBase) {
        super(blockTileBase);
        addInventory(this.augmentInventory = (SidedInvHandler) getAugmentFactory().create().setTile(this).setInputFilter((stack, integer) -> stack.getItem() instanceof IAugment && canAcceptAugment((IAugment) stack.getItem())));
        addGuiAddonFactory(getAugmentBackground());
        for (FacingUtil.Sideness value : FacingUtil.Sideness.values()) {
            augmentInventory.getFacingModes().put(value, IFacingHandler.FaceMode.NONE);
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
    public boolean canAcceptAugment(IAugment augment) {
        return augment.canWorkIn(this);
    }

    @Override
    public List<IAugment> getInstalledAugments() {
        return getItemStackAugments().stream().filter(item -> item.getItem() instanceof IAugment).map(stack -> (IAugment) stack.getItem()).collect(Collectors.toList());
    }

    @Override
    public List<IAugment> getInstalledAugments(IAugmentType filter) {
        return getInstalledAugments().stream().filter(iAugment -> iAugment.getAugmentType().getType().equals(filter.getType())).collect(Collectors.toList());
    }

    @Override
    public boolean hasAugmentInstalled(IAugmentType augmentType) {
        return getInstalledAugments(augmentType).size() > 0;
    }

    public IFactory<PosInvHandler> getAugmentFactory() {
        return () -> new SidedInvHandler("augments", 180, 11, 4, 0).disableFacingAddon().setColor(DyeColor.PURPLE).setRange(1, 4);
    }

    public IFactory<? extends IGuiAddon> getAugmentBackground() {
        return () -> new AssetGuiAddon(AssetTypes.AUGMENT_BACKGROUND, 175, 4, true);
    }

    private List<ItemStack> getItemStackAugments() {
        List<ItemStack> augments = new ArrayList<>();
        for (int i = 0; i < augmentInventory.getSlots(); i++) {
            augments.add(augmentInventory.getStackInSlot(i).copy());
        }
        return augments;
    }

    @Override
    public ActionResultType onActivated(PlayerEntity playerIn, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (super.onActivated(playerIn, hand, facing, hitX, hitY, hitZ) == ActionResultType.SUCCESS) return ActionResultType.SUCCESS;
        openGui(playerIn);
        return ActionResultType.SUCCESS;
    }

}
