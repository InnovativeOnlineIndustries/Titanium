/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
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
import com.hrznstudio.titanium.component.IComponentBundle;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public abstract class ActiveTile<T extends ActiveTile<T>> extends BasicTile<T> implements IScreenAddonProvider,
    ITickableBlockEntity<T>, MenuProvider, IButtonHandler, IFacingComponentHarness, IContainerAddonProvider,
    IHasAssetProvider {

    private MultiInventoryComponent<T> multiInventoryComponent;
    private MultiProgressBarHandler<T> multiProgressBarHandler;
    private MultiTankComponent<T> multiTankComponent;
    private MultiButtonComponent multiButtonComponent;
    private MultiFilterComponent multiFilterComponent;

    private List<IFactory<? extends IScreenAddon>> guiAddons;

    private List<IFactory<? extends IContainerAddon>> containerAddons;

    private List<IComponentBundle> bundles;

    public ActiveTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(base, blockEntityType,pos, state);
        this.guiAddons = new ArrayList<>();
        this.containerAddons = new ArrayList<>();
        this.bundles = new ArrayList<>();
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (multiTankComponent != null && FluidUtil.interactWithFluidHandler(player, hand, multiTankComponent.getCapabilityForSide(null).orElse(new MultiTankComponent.MultiTankCapabilityHandler(new ArrayList<>())))) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(Player player) {
        if (player instanceof ServerPlayer) {
            NetworkHooks.openGui((ServerPlayer) player, this, buffer ->
                LocatorFactory.writePacketBuffer(buffer, new TileEntityLocatorInstance(this.worldPosition)));
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int menu, Inventory inventoryPlayer, Player entityPlayer) {
        return new BasicAddonContainer(this, new TileEntityLocatorInstance(this.worldPosition), this.getWorldPosCallable(),
            inventoryPlayer, menu);
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return new TranslatableComponent(getBasicTileBlock().getDescriptionId()).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
    }

    /*
            Capability Handling
         */
    public void addInventory(InventoryComponent<T> handler) {
        if (multiInventoryComponent == null) multiInventoryComponent = new MultiInventoryComponent<>();
        multiInventoryComponent.add(handler.setComponentHarness(this.getSelf()));
    }

    public void addProgressBar(ProgressBarComponent<T> progressBarComponent) {
        if (multiProgressBarHandler == null) multiProgressBarHandler = new MultiProgressBarHandler<>();
        multiProgressBarHandler.add(progressBarComponent.setComponentHarness(this.getSelf()));
    }

    public void addTank(FluidTankComponent<T> tank) {
        if (multiTankComponent == null) multiTankComponent = new MultiTankComponent<T>();
        multiTankComponent.add(tank.setComponentHarness(this.getSelf()));
    }

    public void addButton(ButtonComponent button) {
        if (multiButtonComponent == null) multiButtonComponent = new MultiButtonComponent();
        multiButtonComponent.add(button);
    }

    public void addFilter(IFilter<?> filter) {
        if (multiFilterComponent == null) {
            multiFilterComponent = new MultiFilterComponent();
        }
        multiFilterComponent.add(filter);
    }

    public void addBundle(IComponentBundle bundle) {
        if (multiInventoryComponent == null) multiInventoryComponent = new MultiInventoryComponent<>();
        if (multiProgressBarHandler == null) multiProgressBarHandler = new MultiProgressBarHandler<>();
        if (multiTankComponent == null) multiTankComponent = new MultiTankComponent<T>();
        if (multiButtonComponent == null) multiButtonComponent = new MultiButtonComponent();
        if (multiFilterComponent == null) multiFilterComponent = new MultiFilterComponent();
        bundle.accept(multiInventoryComponent, multiProgressBarHandler, multiTankComponent, multiButtonComponent, multiFilterComponent);
        bundle.getContainerAddons().forEach(this::addContainerAddonFactory);
        this.bundles.add(bundle);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initClient() {
        super.initClient();
        this.bundles.stream().forEach(iComponentBundle -> iComponentBundle.getScreenAddons().forEach(this::addGuiAddonFactory));
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

    @OnlyIn(Dist.CLIENT)
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
    public void serverTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (multiProgressBarHandler != null) multiProgressBarHandler.update();
        if (level.getGameTime() % getFacingHandlerWorkTime() == 0) {
            if (multiInventoryComponent != null) {
                for (InventoryComponent<T> inventoryHandler : multiInventoryComponent.getInventoryHandlers()) {
                    if (inventoryHandler instanceof IFacingComponent) {
                        if (((IFacingComponent) inventoryHandler).work(this.level, this.worldPosition, this.getFacingDirection(), getFacingHandlerWorkAmount()))
                            break;
                    }
                }
            }
            if (multiTankComponent != null) {
                for (FluidTankComponent<T> tank : multiTankComponent.getTanks()) {
                    if (tank instanceof IFacingComponent) {
                        if (((IFacingComponent) tank).work(this.level, this.worldPosition, this.getFacingDirection(), getFacingHandlerWorkAmount()))
                            break;
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
        return this.level.getBlockState(worldPosition).hasProperty(RotatableBlock.FACING_ALL) ? this.level.getBlockState(worldPosition).getValue(RotatableBlock.FACING_ALL) : (this.level.getBlockState(worldPosition).hasProperty(RotatableBlock.FACING_HORIZONTAL) ? this.level.getBlockState(worldPosition).getValue(RotatableBlock.FACING_HORIZONTAL) : Direction.NORTH);
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
    public void handleButtonMessage(int id, Player playerEntity, CompoundTag compound) {
        if (id == -3) {
            if (!compound.contains("Invalid") && compound.contains("Fill") && !playerEntity.inventoryMenu.getCarried().isEmpty()) {
                boolean fill = compound.getBoolean("Fill");
                String name = compound.getString("Name");
                if (multiTankComponent != null) {
                    for (FluidTankComponent<T> fluidTankComponent : multiTankComponent.getTanks()) {
                        if (fluidTankComponent.getName().equalsIgnoreCase(name))
                            playerEntity.inventoryMenu.getCarried().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(iFluidHandlerItem -> {
                                if (fill) {
                                    int amount = Minecraft.getInstance().player.inventoryMenu.getCarried().getItem() instanceof BucketItem ? FluidAttributes.BUCKET_VOLUME : Integer.MAX_VALUE;
                                    amount = fluidTankComponent.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE);
                                    iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                                } else {
                                    int amount = Minecraft.getInstance().player.inventoryMenu.getCarried().getItem() instanceof BucketItem ? FluidAttributes.BUCKET_VOLUME : Integer.MAX_VALUE;
                                    amount = iFluidHandlerItem.fill(fluidTankComponent.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE);
                                    fluidTankComponent.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                                }
                                playerEntity.inventoryMenu.setCarried(iFluidHandlerItem.getContainer().copy());
                                if (playerEntity instanceof ServerPlayer) {
                                    //((ServerPlayer) playerEntity).broadcastCarriedItem(); TODO
                                }
                            });
                    }
                }
            }
        }
        if (id == -2) {
            String name = compound.getString("Name");
            if (multiFilterComponent != null) {
                for (IFilter<?> filter : multiFilterComponent.getFilters()) {
                    if (filter.getName().equals(name)) {
                        int slot = compound.getInt("Slot");
                        filter.setFilter(slot, ItemStack.of(compound.getCompound("Filter")));
                        markForUpdate();
                        break;
                    }
                }
            }
        }
        if (id == -1) {
            String name = compound.getString("Name");
            FacingUtil.Sideness facing = FacingUtil.Sideness.valueOf(compound.getString("Facing"));
            int faceMode = compound.getInt("Next");
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
    public Level getComponentWorld() {
        return getSelf().getLevel();
    }

    @Override
    public void markComponentDirty() {
        super.setChanged();
    }

    @Override
    public void markComponentForUpdate(boolean referenced) {
        if (!referenced) {
            super.markForUpdate();
        } else {
            this.markComponentDirty();
        }
    }

    public ContainerLevelAccess getWorldPosCallable() {
        return this.getLevel() != null ? ContainerLevelAccess.create(this.getLevel(), this.getBlockPos()) : ContainerLevelAccess.NULL;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.multiInventoryComponent != null)
            this.multiInventoryComponent.getLazyOptionals().forEach(LazyOptional::invalidate);
        if (this.multiTankComponent != null)
            this.multiTankComponent.getLazyOptionals().forEach(LazyOptional::invalidate);
    }

    public boolean canInteract() {
        return this.level.getBlockEntity(this.worldPosition) == this;
    }


}
