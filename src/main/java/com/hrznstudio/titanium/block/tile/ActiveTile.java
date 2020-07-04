/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.client.IScreenAddon;
import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.api.filter.IFilter;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IHasAssetProvider;
import com.hrznstudio.titanium.component.button.ButtonComponent;
import com.hrznstudio.titanium.component.button.MultiButtonComponent;
import com.hrznstudio.titanium.component.filter.MultiFilterComponent;
import com.hrznstudio.titanium.component.fluid.FluidTankComponent;
import com.hrznstudio.titanium.component.fluid.MultiTankComponent;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.component.inventory.MultiInventoryComponent;
import com.hrznstudio.titanium.component.progress.MultiProgressBarHandler;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponent;
import com.hrznstudio.titanium.component.sideness.IFacingComponentHarness;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.container.addon.IContainerAddon;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.network.IButtonHandler;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.instance.TileEntityLocatorInstance;
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
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public abstract class ActiveTile<T extends ActiveTile<T>> extends BasicTile<T> implements IScreenAddonProvider,
    ITickableTileEntity, INamedContainerProvider, IButtonHandler, IFacingComponentHarness, IContainerAddonProvider,
    IHasAssetProvider {

    private MultiInventoryComponent<T> multiInventoryComponent;
    private MultiProgressBarHandler<T> multiProgressBarHandler;
    private MultiTankComponent<T> multiTankComponent;
    private MultiButtonComponent multiButtonComponent;
    private MultiFilterComponent multiFilterComponent;

    private List<IFactory<? extends IScreenAddon>> guiAddons;

    private List<IFactory<? extends IContainerAddon>> containerAddons;

    public ActiveTile(BasicTileBlock<T> base) {
        super(base);
        this.guiAddons = new ArrayList<>();
        this.containerAddons = new ArrayList<>();
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType onActivated(PlayerEntity player, Hand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (multiTankComponent != null && FluidUtil.interactWithFluidHandler(player, hand, multiTankComponent.getCapabilityForSide(null).orElse(new MultiTankComponent.MultiTankCapabilityHandler(new ArrayList<>())))) {
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, this, buffer ->
                    LocatorFactory.writePacketBuffer(buffer, new TileEntityLocatorInstance(this.pos)));
        }
    }

    @Nullable
    @Override
    public Container createMenu(int menu, PlayerInventory inventoryPlayer, PlayerEntity entityPlayer) {
        return new BasicAddonContainer(this, new TileEntityLocatorInstance(this.pos), this.getWorldPosCallable(),
                inventoryPlayer, menu);
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(getBasicTileBlock().getTranslationKey()).func_230530_a_(Style.field_240709_b_.func_240712_a_(TextFormatting.DARK_GRAY));
    }

    /*
            Capability Handling
         */
    public void addInventory(InventoryComponent<T> handler) {
        if (multiInventoryComponent == null) {
            multiInventoryComponent = new MultiInventoryComponent<>();
        }
        multiInventoryComponent.add(handler.setComponentHarness(this.getSelf()));
    }

    public void addProgressBar(ProgressBarComponent<T> progressBarComponent) {
        if (multiProgressBarHandler == null) {
            multiProgressBarHandler = new MultiProgressBarHandler<>();
        }
        multiProgressBarHandler.addBar(progressBarComponent.setComponentHarness(this.getSelf()));
    }

    public void addTank(FluidTankComponent<T> tank) {
        if (multiTankComponent == null) multiTankComponent = new MultiTankComponent<T>();
        multiTankComponent.add(tank.setComponentHarness(this.getSelf()));
    }

    public void addButton(ButtonComponent button) {
        if (multiButtonComponent == null) multiButtonComponent = new MultiButtonComponent();
        multiButtonComponent.addButton(button);
    }

    public void addFilter(IFilter<?> filter) {
        if (multiFilterComponent == null) {
            multiFilterComponent = new MultiFilterComponent();
        }
        multiFilterComponent.add(filter);
    }

    @Nonnull
    @Override
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && multiInventoryComponent != null) {
            return multiInventoryComponent.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), side)).cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && multiTankComponent != null) {
            return multiTankComponent.getCapabilityForSide(FacingUtil.getFacingRelative(this.getFacingDirection(), side)).cast();
        }
        return LazyOptional.empty();
    }

    public MultiInventoryComponent<T> getMultiInventoryComponent() {
        return multiInventoryComponent;
    }

    /*
        Client
     */

    public void addGuiAddonFactory(IFactory<? extends IScreenAddon> factory) {
        this.guiAddons.add(factory);
    }

    public void addContainerAddonFactory(IFactory<? extends IContainerAddon> factory) {
        this.containerAddons.add(factory);
    }


    @Override
    public List<IFactory<? extends IScreenAddon>> getScreenAddons() {
        List<IFactory<? extends IScreenAddon>> addons = new ArrayList<>(guiAddons);
        if (multiInventoryComponent != null) addons.addAll(multiInventoryComponent.getScreenAddons());
        if (multiProgressBarHandler != null) addons.addAll(multiProgressBarHandler.getScreenAddons());
        if (multiTankComponent != null) addons.addAll(multiTankComponent.getScreenAddons());
        if (multiButtonComponent != null) addons.addAll(multiButtonComponent.getScreenAddons());
        if (multiFilterComponent != null) addons.addAll(multiFilterComponent.getScreenAddons());
        return addons;
    }

    @Override
    public List<IFactory<? extends IContainerAddon>> getContainerAddons() {
        List<IFactory<? extends IContainerAddon>> addons = new ArrayList<>(containerAddons);
        if (multiInventoryComponent != null) addons.addAll(multiInventoryComponent.getContainerAddons());
        if (multiProgressBarHandler != null) addons.addAll(multiProgressBarHandler.getContainerAddons());
        if (multiTankComponent != null) addons.addAll(multiTankComponent.getContainerAddons());
        return addons;
    }

    @Override
    public IAssetProvider getAssetProvider() {
        return IAssetProvider.DEFAULT_PROVIDER;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (multiProgressBarHandler != null) multiProgressBarHandler.update();
            if (world.getGameTime() % getFacingHandlerWorkTime() == 0) {
                if (multiInventoryComponent != null) {
                    for (InventoryComponent<T> inventoryHandler : multiInventoryComponent.getInventoryHandlers()) {
                        if (inventoryHandler instanceof IFacingComponent) {
                            if (((IFacingComponent) inventoryHandler).work(this.world, this.pos, this.getFacingDirection(), getFacingHandlerWorkAmount()))
                                break;
                        }
                    }
                }
                if (multiTankComponent != null) {
                    for (FluidTankComponent<T> tank : multiTankComponent.getTanks()) {
                        if (tank instanceof IFacingComponent) {
                            if (((IFacingComponent) tank).work(this.world, this.pos, this.getFacingDirection(), getFacingHandlerWorkAmount()))
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

    public MultiButtonComponent getMultiButtonComponent() {
        return multiButtonComponent;
    }

    public Direction getFacingDirection() {
        return this.world.getBlockState(pos).func_235901_b_(RotatableBlock.FACING_ALL) ? this.world.getBlockState(pos).get(RotatableBlock.FACING_ALL) : (this.world.getBlockState(pos).func_235901_b_(RotatableBlock.FACING_HORIZONTAL) ? this.world.getBlockState(pos).get(RotatableBlock.FACING_HORIZONTAL) : Direction.NORTH);
    }

    @Override
    public IFacingComponent getHandlerFromName(String string) {
        if (multiInventoryComponent != null) {
            for (InventoryComponent<T> handler : multiInventoryComponent.getInventoryHandlers()) {
                if (handler instanceof IFacingComponent && handler.getName().equalsIgnoreCase(string))
                    return (IFacingComponent) handler;
            }
        }
        if (multiTankComponent != null) {
            for (FluidTankComponent<T> fluidTankComponent : multiTankComponent.getTanks()) {
                if (fluidTankComponent instanceof IFacingComponent && fluidTankComponent.getName().equalsIgnoreCase(string))
                    return (IFacingComponent) fluidTankComponent;
            }
        }
        return null;
    }

    @Override
    public void handleButtonMessage(int id, PlayerEntity playerEntity, CompoundNBT compound) {
        if (id == -2) {
            String name = compound.getString("Name");
            if (multiFilterComponent != null) {
                for (IFilter<?> filter : multiFilterComponent.getFilters()) {
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
            IFacingComponent.FaceMode faceMode = IFacingComponent.FaceMode.values()[compound.getInt("Next")];
            if (multiInventoryComponent != null && multiInventoryComponent.handleFacingChange(name, facing, faceMode)) {
                markForUpdate();
            } else if (multiTankComponent != null && multiTankComponent.handleFacingChange(name, facing, faceMode)) {
                markForUpdate();
            }
        } else if (multiButtonComponent != null) {
            multiButtonComponent.clickButton(id, playerEntity, compound);
        }
    }

    @Nonnull
    public abstract T getSelf();

    @Override
    public World getComponentWorld() {
        return getSelf().getWorld();
    }

    @Override
    public void markComponentDirty() {
        super.markDirty();
    }

    @Override
    public void markComponentForUpdate(boolean referenced) {
        if (!referenced) {
            super.markForUpdate();
        } else {
            this.markComponentDirty();
        }
    }

    public IWorldPosCallable getWorldPosCallable() {
        return this.getWorld() != null ? IWorldPosCallable.of(this.getWorld(), this.getPos()) : IWorldPosCallable.DUMMY;
    }

}
