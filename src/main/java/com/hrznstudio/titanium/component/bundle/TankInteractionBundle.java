/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component.bundle;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.block.tile.BasicTile;
import com.hrznstudio.titanium.client.screen.addon.AssetScreenAddon;
import com.hrznstudio.titanium.component.IComponentBundle;
import com.hrznstudio.titanium.component.IComponentHandler;
import com.hrznstudio.titanium.component.IComponentHarness;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.util.TitaniumFluidUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class TankInteractionBundle<T extends BasicTile & IComponentHarness> implements IComponentBundle, INBTSerializable<CompoundTag> {

    private final Supplier<LazyOptional<IFluidHandler>> fluidHandler;
    private int posX;
    private int posY;
    private InventoryComponent<T> input;
    private InventoryComponent<T> output;
    private ProgressBarComponent<T> bar;

    public TankInteractionBundle(Supplier<LazyOptional<IFluidHandler>> fluidHandler, int posX, int posY, T componentHarness, int maxProgress) {
        this.fluidHandler = fluidHandler;
        this.posX = posX;
        this.posY = posY;
        this.input = new InventoryComponent<T>("tank_input", posX + 5, posY + 7, 1)
            .setSlotToItemStackRender(0, new ItemStack(Items.BUCKET))
            .setOutputFilter((stack, integer) -> false)
            .setSlotToColorRender(0, DyeColor.BLUE)
            .setInputFilter((stack, integer) -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent())
            .setComponentHarness(componentHarness);
        this.output = new InventoryComponent<T>("tank_output", posX + 5, posY + 60, 1)
            .setSlotToItemStackRender(0, new ItemStack(Items.BUCKET))
            .setInputFilter((stack, integer) -> false)
            .setSlotToColorRender(0, DyeColor.ORANGE)
            .setComponentHarness(componentHarness);
        this.bar = new ProgressBarComponent<T>(posX + 5, posY + 30, maxProgress)
            .setBarDirection(ProgressBarComponent.BarDirection.ARROW_DOWN)
            .setCanReset(t -> true)
            .setCanIncrease(t -> !this.input.getStackInSlot(0).isEmpty() && this.input.getStackInSlot(0).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() && !getOutputStack(false).isEmpty() && (this.output.getStackInSlot(0).isEmpty() || ItemHandlerHelper.canItemStacksStack(getOutputStack(false), this.output.getStackInSlot(0))))
            .setOnFinishWork(() -> {
                ItemStack result = getOutputStack(false);
                if (ItemHandlerHelper.insertItem(this.output, result, true).isEmpty()) {
                    result = getOutputStack(true);
                    ItemHandlerHelper.insertItem(this.output, result, false);
                    this.input.getStackInSlot(0).shrink(1);
                    componentHarness.setChanged();
                }
            })
            .setComponentHarness(componentHarness);

    }

    @Override
    public void accept(IComponentHandler... handler) {
        for (IComponentHandler iComponentHandler : handler) {
            iComponentHandler.add(this.input, this.output, this.bar);
        }
    }

    public ItemStack getOutputStack(boolean execute) {
        return fluidHandler.get().map(iFluidHandler -> {
            ItemStack stack = this.input.getStackInSlot(0).copy();
            stack.setCount(1);
            FluidActionResult result = FluidUtil.tryFillContainer(stack, iFluidHandler, Integer.MAX_VALUE, null, execute);
            if (result.isSuccess()) return result.getResult();
            result = TitaniumFluidUtil.tryEmptyContainer(stack, iFluidHandler, Integer.MAX_VALUE, execute);
            if (result.isSuccess()) return result.getResult();
            return ItemStack.EMPTY;
        }).orElse(ItemStack.EMPTY);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        return Collections.singletonList(() -> new AssetScreenAddon(AssetTypes.AUGMENT_BACKGROUND, posX, posY, true));
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        return Collections.emptyList();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.put("Input", this.input.serializeNBT());
        compoundNBT.put("Output", this.output.serializeNBT());
        compoundNBT.put("Bar", this.bar.serializeNBT());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.input.deserializeNBT(nbt.getCompound("Input"));
        this.output.deserializeNBT(nbt.getCompound("Output"));
        this.bar.deserializeNBT(nbt.getCompound("Bar"));
    }
}
