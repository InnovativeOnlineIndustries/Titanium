/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IGuiAddon;
import com.hrznstudio.titanium.api.client.IGuiAddonProvider;
import com.hrznstudio.titanium.block.BlockRotation;
import com.hrznstudio.titanium.block.BlockTileBase;
import com.hrznstudio.titanium.block.tile.button.MultiButtonHandler;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.fluid.MultiTankHandler;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.MultiInventoryHandler;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.client.gui.GuiContainerTile;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import com.hrznstudio.titanium.container.ContainerTileBase;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileActive extends TileBase implements IGuiAddonProvider, ITickableTileEntity, INamedContainerProvider {

    private MultiInventoryHandler multiInventoryHandler;
    private MultiProgressBarHandler multiProgressBarHandler;
    private MultiTankHandler multiTankHandler;
    private MultiButtonHandler multiButtonHandler;

    private List<IFactory<? extends IGuiAddon>> guiAddons;

    public TileActive(BlockTileBase base) {
        super(base);
        this.guiAddons = new ArrayList<>();
    }

    @Override
    public boolean onActivated(PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (multiTankHandler != null) {
            return FluidUtil.interactWithFluidHandler(playerIn, hand, multiTankHandler.getCapabilityForSide(null));
        }
        return false;
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity)
            Titanium.openGui(this, (ServerPlayerEntity) player);
    }

    @Nullable
    @Override
    public Container createMenu(int menu, PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
        return new ContainerTileBase<>(this, inventoryPlayer);
    }

    public Screen createGui(Container container) {
        return new GuiContainerTile<>((ContainerTileBase<TileActive>) container);
    }


    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("what. pls");
    }

    /*
            Capability Handling
         */
    public void addInventory(PosInvHandler handler) {
        if (multiInventoryHandler == null) multiInventoryHandler = new MultiInventoryHandler();
        multiInventoryHandler.addInventory(handler.setTile(this));
    }

    public void addProgressBar(PosProgressBar posProgressBar) {
        if (multiProgressBarHandler == null) multiProgressBarHandler = new MultiProgressBarHandler();
        multiProgressBarHandler.addBar(posProgressBar.setTile(this));
    }

    public void addTank(PosFluidTank tank) {
        if (multiTankHandler == null) multiTankHandler = new MultiTankHandler();
        multiTankHandler.addTank(tank);
    }

    public void addButton(PosButton button) {
        if (multiButtonHandler == null) multiButtonHandler = new MultiButtonHandler();
        multiButtonHandler.addButton(button);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && multiInventoryHandler != null) {
            return LazyOptional.of(new NonNullSupplier<T>() {
                @Nonnull
                @Override
                public T get() {
                    return (T) multiInventoryHandler.getCapabilityForSide(FacingUtil.getFacingRelative(TileActive.this.getFacingDirection(), side));
                }
            });
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && multiTankHandler != null) {
            return LazyOptional.of(new NonNullSupplier<T>() {
                @Nonnull
                @Override
                public T get() {
                    return (T) multiTankHandler.getCapabilityForSide(FacingUtil.getFacingRelative(TileActive.this.getFacingDirection(), side));
                }
            });
        }
        return LazyOptional.empty();
    }

    public MultiInventoryHandler getMultiInventoryHandler() {
        return multiInventoryHandler;
    }

    /*
        Client
     */

    public void addGuiAddonFactory(IFactory<? extends IGuiAddon> factory) {
        this.guiAddons.add(factory);
    }

    @Override
    public List<IFactory<? extends IGuiAddon>> getGuiAddons() {
        List<IFactory<? extends IGuiAddon>> addons = new ArrayList<>(guiAddons);
        if (multiInventoryHandler != null) addons.addAll(multiInventoryHandler.getGuiAddons());
        if (multiProgressBarHandler != null) addons.addAll(multiProgressBarHandler.getGuiAddons());
        if (multiTankHandler != null) addons.addAll(multiTankHandler.getGuiAddons());
        if (multiButtonHandler != null) addons.addAll(multiButtonHandler.getGuiAddons());
        return addons;
    }

    public IAssetProvider getAssetProvider() {
        return IAssetProvider.DEFAULT_PROVIDER;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (multiProgressBarHandler != null) multiProgressBarHandler.update();
            if (world.getGameTime() % getFacingHandlerWorkTime() == 0) {
                if (multiInventoryHandler != null) {
                    for (PosInvHandler inventoryHandler : multiInventoryHandler.getInventoryHandlers()) {
                        if (inventoryHandler instanceof IFacingHandler) {
                            if (((IFacingHandler) inventoryHandler).work(this.world, this.pos, this.getFacingDirection(), getFacingHandlerWorkAmount()))
                                break;
                        }
                    }
                }
                if (multiButtonHandler != null) {
                    for (PosFluidTank tank : multiTankHandler.getTanks()) {
                        if (tank instanceof IFacingHandler) {
                            if (((IFacingHandler) tank).work(this.world, this.pos, this.getFacingDirection(), getFacingHandlerWorkAmount()))
                                break;
                        }
                    }
                }
            }
        }
    }

    public int getFacingHandlerWorkTime() {
        return 10;
    }

    public int getFacingHandlerWorkAmount() {
        return 4;
    }

    public MultiButtonHandler getMultiButtonHandler() {
        return multiButtonHandler;
    }

    public Direction getFacingDirection() {
        return this.world.getBlockState(pos).get(BlockRotation.FACING);
    }

    public IFacingHandler getHandlerFromName(String string) {
        for (PosInvHandler handler : multiInventoryHandler.getInventoryHandlers()) {
            if (handler instanceof IFacingHandler && handler.getName().equalsIgnoreCase(string))
                return (IFacingHandler) handler;
        }
        for (PosFluidTank posFluidTank : multiTankHandler.getTanks()) {
            if (posFluidTank instanceof IFacingHandler && posFluidTank.getName().equalsIgnoreCase(string))
                return (IFacingHandler) posFluidTank;
        }
        return null;
    }

    public void handleButtonMessage(int id, CompoundNBT compound) {
        if (id == -1) {
            String name = compound.getString("Name");
            FacingUtil.Sideness facing = FacingUtil.Sideness.valueOf(compound.getString("Facing"));
            IFacingHandler.FaceMode faceMode = IFacingHandler.FaceMode.values()[compound.getInt("Next")];
            IFacingHandler facingHandler = getHandlerFromName(name);
            if (facingHandler != null) {
                facingHandler.getFacingModes().put(facing, faceMode);
                markForUpdate();
            }
        } else if (multiButtonHandler != null) {
            multiButtonHandler.clickButton(id, compound);
        }
    }
}