/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

public class PosInvHandler extends ItemStackHandler {

    private final String name;
    private int xPos;
    private int yPos;
    private int xSize;
    private int ySize;
    private TileEntity tileEntity;
    private BiPredicate<ItemStack, Integer> insertPredicate;
    private BiPredicate<ItemStack, Integer> extractPredicate;

    public PosInvHandler(String name, int xPos, int yPos, int size) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.setSize(size);
        this.setRange(size, 1);
        this.insertPredicate = (stack, integer) -> true;
        this.extractPredicate = (stack, integer) -> true;
    }

    public PosInvHandler setRange(int x, int y) {
        this.xSize = x;
        this.ySize = y;
        return this;
    }

    public PosInvHandler setTile(TileEntity tile) {
        this.tileEntity = tile;
        return this;
    }

    public PosInvHandler setInputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.insertPredicate = predicate;
        return this;
    }

    public PosInvHandler setOutputFilter(BiPredicate<ItemStack, Integer> predicate) {
        this.extractPredicate = predicate;
        return this;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!insertPredicate.test(stack, slot)) return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.tileEntity != null) tileEntity.markDirty();
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
}