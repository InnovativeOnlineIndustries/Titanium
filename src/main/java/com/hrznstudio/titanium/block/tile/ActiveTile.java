/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.button.MultiButtonHandler;
import com.hrznstudio.titanium.block.tile.button.PosButton;
import com.hrznstudio.titanium.block.tile.filter.MultiFilterHandler;
import com.hrznstudio.titanium.block.tile.fluid.MultiTankHandler;
import com.hrznstudio.titanium.block.tile.fluid.PosFluidTank;
import com.hrznstudio.titanium.block.tile.inventory.MultiInventoryHandler;
import com.hrznstudio.titanium.block.tile.inventory.PosInvHandler;
import com.hrznstudio.titanium.block.tile.progress.IProgressing;
import com.hrznstudio.titanium.block.tile.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import com.hrznstudio.titanium.block.tile.sideness.IFacingHandler;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.container.impl.ContainerTileBase;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.util.FacingUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class ActiveTile<T extends BasicTileBlock<T, U>, U extends ActiveTile<T, U>> extends BasicTile<T, U> implements IScreenAddonProvider,
        ITickableTileEntity, INamedContainerProvider, IButtonHandler, IProgressing {

    private MultiInventoryHandler multiInventoryHandler;
    private MultiProgressBarHandler<U> multiProgressBarHandler;
    private MultiTankHandler multiTankHandler;
    private MultiButtonHandler multiButtonHandler;
    private MultiFilterHandler multiFilterHandler;

    private List<IFactory<? extends IScreenAddon>> screenAddons;

    public ActiveTile(T base) {
        super(base);
        this.screenAddons = new ArrayList<>();
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (checkTank(player, hand)){
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean checkTank(PlayerEntity player, Hand hand) {
        if (multiTankHandler != null) {
            return multiTankHandler.getCapabilityForSide(null)
                    .map(handler -> FluidUtil.interactWithFluidHandler(player, hand, handler))
                    .orElse(false);
        } else {
            return false;
        }
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            Titanium.openGui(this, (ServerPlayerEntity) player);
        }
    }

    @Nullable
    @Override
    public Container createMenu(int menu, PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
        return new ContainerTileBase(this, inventoryPlayer, menu);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(getBlockTileBase().getTranslationKey()).setStyle(new Style().setColor(TextFormatting.DARK_GRAY));
    }

    /*
    * Capability Handling
    */
    public void addInventory(PosInvHandler handler) {
        if (multiInventoryHandler == null) multiInventoryHandler = new MultiInventoryHandler();
        multiInventoryHandler.add(handler.setTile(this));
    }

    public void addProgressBar(PosProgressBar<U> posProgressBar) {
        if (multiProgressBarHandler == null) {
            multiProgressBarHandler = new MultiProgressBarHandler<>();
        }
        multiProgressBarHandler.addBar(posProgressBar.setProgressing((U) this));
    }

    public void addTank(PosFluidTank tank) {
        if (multiTankHandler == null) multiTankHandler = new MultiTankHandler();
        multiTankHandler.add(tank.setTile(this));
    }

    public void addButton(PosButton button) {
        if (multiButtonHandler == null) multiButtonHandler = new MultiButtonHandler();
        multiButtonHandler.addButton(button);
    }

    public void addFilter(IFilter filter) {
        if (multiFilterHandler == null) multiFilterHandler = new MultiFilterHandler();
        multiFilterHandler.add(filter);
    }

    @Nonnull
    @Override
    public <V> LazyOptional<V> getCapability(@Nonnull Capability<V> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && multiInventoryHandler != null) {
            return multiInventoryHandler.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), side)).cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && multiTankHandler != null) {
            return multiTankHandler.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), side)).cast();
        }
        return LazyOptional.empty();
    }

    public MultiInventoryHandler getMultiInventoryHandler() {
        return multiInventoryHandler;
    }

    /*
        Client
     */

    public void addScreenAddonFactory(IFactory<? extends IScreenAddon> factory) {
        this.screenAddons.add(factory);
    }

    @Override
    public List<IFactory<? extends IScreenAddon>> getAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>(screenAddons);
        if (multiInventoryHandler != null) addons.addAll(multiInventoryHandler.getAddons());
        if (multiProgressBarHandler != null) addons.addAll(multiProgressBarHandler.getAddons());
        if (multiTankHandler != null) addons.addAll(multiTankHandler.getAddons());
        if (multiButtonHandler != null) addons.addAll(multiButtonHandler.getAddons());
        if (multiFilterHandler != null) addons.addAll(multiFilterHandler.getAddons());
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
                if (multiTankHandler != null) {
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
        return this.world.getBlockState(pos).has(RotatableBlock.FACING) ? this.world.getBlockState(pos).get(RotatableBlock.FACING) : Direction.NORTH;
    }

    public IFacingHandler getHandlerFromName(String string) {
        if (multiInventoryHandler != null) {
            for (PosInvHandler handler : multiInventoryHandler.getInventoryHandlers()) {
                if (handler instanceof IFacingHandler && handler.getName().equalsIgnoreCase(string))
                    return (IFacingHandler) handler;
            }
        }
        if (multiTankHandler != null) {
            for (PosFluidTank posFluidTank : multiTankHandler.getTanks()) {
                if (posFluidTank instanceof IFacingHandler && posFluidTank.getName().equalsIgnoreCase(string))
                    return (IFacingHandler) posFluidTank;
            }
        }
        return null;
    }

    @Override
    public void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        if (id == -2) {
            String name = compound.getString("Name");
            if (multiFilterHandler != null) {
                for (IFilter filter : multiFilterHandler.getFilters()) {
                    if (filter.getName().equals(name)) {
                        int slot = compound.getInt("Slot");
                        filter.setFilter(slot, ItemStack.read(compound.getCompound("Filter")));
                        markForUpdate();
                        break;
                    }
                }
            }
        }
        if (id == -1) {
            String name = compound.getString("Name");
            FacingUtil.Sideness facing = FacingUtil.Sideness.valueOf(compound.getString("Facing"));
            IFacingHandler.FaceMode faceMode = IFacingHandler.FaceMode.values()[compound.getInt("Next")];
            if (multiInventoryHandler != null && multiInventoryHandler.handleFacingChange(name, facing, faceMode)) {
                markForUpdate();
            } else if (multiTankHandler != null && multiTankHandler.handleFacingChange(name, facing, faceMode)) {
                markForUpdate();
            }
        } else if (multiButtonHandler != null) {
            multiButtonHandler.clickButton(id, playerEntity, compound);
        }
    }
}