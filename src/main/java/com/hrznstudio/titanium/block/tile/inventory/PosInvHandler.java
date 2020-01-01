/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile.inventory;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.client.gui.addon.SlotsGuiAddon;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class PosInvHandler extends ItemStackHandler implements IGuiAddonProvider {

    private final String name;
    private int xPos;
    private int yPos;
    private int xSize;
    private int ySize;
    private TileEntity tileEntity;
    private BiPredicate<ItemStack, Integer> insertPredicate;
    private BiPredicate<ItemStack, Integer> extractPredicate;
    private BiConsumer<ItemStack, Integer> onSlotChanged;
    private HashMap<Integer, Integer> slotAmountFilter;
    private int slotLimit;
    private Function<Integer, Pair<Integer, Integer>> slotPosition;

    public PosInvHandler(String name, int xPos, int yPos, int size) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.setSize(size);
        this.setRange(size, 1);
        this.insertPredicate = (stack, integer) -> true;
        this.extractPredicate = (stack, integer) -> true;
        this.onSlotChanged = (stack, integer) -> {
        };
        this.slotAmountFilter = new HashMap<>();
        this.slotLimit = 64;
        this.slotPosition = integer -> Pair.of(18 * (integer % xSize), 18 * (integer / xSize));
    }

    /**
     * Defines how many slots/row and slots/column
     *
     * @param x How many slots there are horizontally
     * @param y How many slots there are vertically
     * @return itself
     */
    public PosInvHandler setRange(int x, int y) {
        this.xSize = x;
        this.ySize = y;
        return this;
    }

    /**
     * Sets the tile where the inventory is to allow markForUpdate automatically
     *
     * @param tile The tile to mark
     * @return itself
     */
    public PosInvHandler setTile(TileEntity tile) {
        this.tileEntity = tile;
        return this;
    }

    /**
     * Sets the predicate input filter to filter what items go into which slot.
     *
     * @param predicate A bi predicate where the itemstack is the item trying to be inserted and the slot where is trying to be inserted to
     * @return itself
     */
    public PosInvHandler setInputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.insertPredicate = predicate;
        return this;
    }

    /**
     * Sets the predicate output filter to filter what can be extracted from which slot.
     *
     * @param predicate A bi predicate where the itemstack is the item trying to be extracted and the slot where is trying to be extracted
     * @return itself
     */
    public PosInvHandler setOutputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.extractPredicate = predicate;
        return this;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        validateSlotIndex(slot);
        ItemStack existingStack = this.stacks.get(slot);
        int limit = getStackLimit(slot, stack);
        if (!existingStack.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existingStack)) {
                return stack;
            }
            limit -= existingStack.getCount();
        }
        if (limit <= 0) {
            return stack;
        }
        boolean reachedLimit = stack.getCount() > limit;
        if (!simulate) {
            if (existingStack.isEmpty()) {
                this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existingStack.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }
        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.tileEntity != null) tileEntity.markDirty();
        onSlotChanged.accept(getStackInSlot(slot), slot);
    }

    public String getName() {
        return name;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public BiPredicate<ItemStack, Integer> getInsertPredicate() {
        return insertPredicate;
    }

    public BiPredicate<ItemStack, Integer> getExtractPredicate() {
        return extractPredicate;
    }

    public BiConsumer<ItemStack, Integer> getOnSlotChanged() {
        return onSlotChanged;
    }

    /**
     * Sets the predicate slot changed that gets triggered when a slot is changed.
     *
     * @param onSlotChanged A bi predicate where the itemstack and slot changed
     * @return itself
     */
    public PosInvHandler setOnSlotChanged(BiConsumer<ItemStack, Integer> onSlotChanged) {
        this.onSlotChanged = onSlotChanged;
        return this;
    }

    /**
     * Sets the limit amount for a specific slot, this limit has priority instead of the slot limit for all the slots
     *
     * @param slot  The slot to set the limit to
     * @param limit The limit for the slot
     * @return itself
     */
    public PosInvHandler setSlotLimit(int slot, int limit) {
        this.slotAmountFilter.put(slot, limit);
        return this;
    }

    /**
     * Sets the default limit for all the slots
     *
     * @param limit The default limit for all the slot that don't have specific limit
     * @return itself
     */
    public PosInvHandler setSlotLimit(int limit) {
        this.slotLimit = limit;
        return this;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotAmountFilter.getOrDefault(slot, this.slotLimit);
    }

    public Function<Integer, Pair<Integer, Integer>> getSlotPosition() {
        return slotPosition;
    }

    public PosInvHandler setSlotPosition(Function<Integer, Pair<Integer, Integer>> slotPosition) {
        this.slotPosition = slotPosition;
        return this;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return insertPredicate.test(stack, slot);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>();
        addons.add(() -> new SlotsGuiAddon(this));
        return addons;
    }
}