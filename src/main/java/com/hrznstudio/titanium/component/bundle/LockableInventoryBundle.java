/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.bundle;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.client.screen.addon.LockableOverlayAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonAddon;
import com.hrznstudio.titanium.client.screen.addon.StateButtonInfo;
import com.hrznstudio.titanium.component.IComponentBundle;
import com.hrznstudio.titanium.component.IComponentHandler;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.util.LangUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class LockableInventoryBundle<T extends BasicTile & IComponentHarness> implements IComponentBundle, INBTSerializable<CompoundNBT> {

    private InventoryComponent<T> inventory;
    private BiPredicate<ItemStack, Integer> cachedFilter;
    private ButtonComponent buttonAddon;
    private T componentHarness;
    private ItemStack[] filter;
    private int lockPosX;
    private int lockPosY;
    private boolean isLocked;

    public LockableInventoryBundle(T componentHarness, InventoryComponent<T> inventory, int lockPosX, int lockPosY, boolean isLocked) {
        this.componentHarness = componentHarness;
        this.inventory = inventory;
        this.cachedFilter = inventory.getInsertPredicate();
        this.filter = new ItemStack[this.inventory.getSlots()];
        Arrays.fill(this.filter, ItemStack.EMPTY);
        this.lockPosX = lockPosX;
        this.lockPosY = lockPosY;
        this.isLocked = isLocked;
        this.buttonAddon = new ButtonComponent(lockPosX, lockPosY, 14,14){
            @Override
            public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
                return Collections.singletonList(() -> new StateButtonAddon(buttonAddon,
                    new StateButtonInfo(0, AssetTypes.BUTTON_UNLOCKED, TextFormatting.GOLD + LangUtil.getString("tooltip.titanium.locks") +  TextFormatting.WHITE +  " " + LangUtil.getString("tooltip.titanium.facing_handler." + inventory.getName().toLowerCase())),
                    new StateButtonInfo(1, AssetTypes.BUTTON_LOCKED, TextFormatting.GOLD + LangUtil.getString("tooltip.titanium.unlocks") + TextFormatting.WHITE + " " + LangUtil.getString("tooltip.titanium.facing_handler." + inventory.getName().toLowerCase()))) {
                    @Override
                    public int getState() {
                        return LockableInventoryBundle.this.isLocked ? 1 : 0;
                    }
                });
            }
        }.setPredicate((playerEntity, compoundNBT) -> {
                this.isLocked = !this.isLocked;
            for (int i = 0; i < this.inventory.getSlots(); i++) {
                this.filter[i] = this.inventory.getStackInSlot(i).copy();
            }
                updateFilter();
                this.componentHarness.syncObject(this);
            });
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return inventory instanceof SidedInventoryComponent ? Collections.singletonList(() -> new LockableOverlayAddon((SidedInventoryComponent) inventory, this.lockPosX, this.lockPosY)) : Collections.emptyList();
    }

    @Override
    public void accept(IComponentHandler... handler) {
        for (IComponentHandler iComponentHandler : handler) {
            iComponentHandler.add(this.inventory, this.buttonAddon);
        }
    }

    @Nonnull
    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Collections.emptyList();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.put("Inventory", this.inventory.serializeNBT());
        compoundNBT.putBoolean("Locked", this.isLocked);
        ListNBT nbt = new ListNBT();
        for (ItemStack stack : this.filter) {
            nbt.add(stack.serializeNBT());
        }
        compoundNBT.put("Filter", nbt);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));
        this.isLocked = nbt.getBoolean("Locked");
        ListNBT list = (ListNBT) nbt.get("Filter");
        this.filter = new ItemStack[list.size()];
        Arrays.fill(this.filter, ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            this.filter[i] = ItemStack.read(list.getCompound(i));
        }
        updateFilter();
    }

    private void updateFilter(){
        if (isLocked){
            this.inventory.setInputFilter((stack, integer) -> integer < this.filter.length && !this.filter[integer].isEmpty() && this.filter[integer].isItemEqual(stack));
        } else {
            Arrays.fill(this.filter, ItemStack.EMPTY);
            this.inventory.setInputFilter(this.cachedFilter);
        }
        for (int i = 0; i < this.filter.length; i++) {
            this.inventory.setSlotToItemStackRender(i, this.filter[i]);
        }
    }

    public InventoryComponent<T> getInventory() {
        return inventory;
    }

}
