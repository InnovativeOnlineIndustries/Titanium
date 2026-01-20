/*
 * This file is part of Titanium
 * Copyright (C) 2025, Horizon Studio <contact@hrznstudio.com>.
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // Оптимизация: отложенное dirty обновление вместо мгновенного setChanged()
    private boolean pendingDirty = false;

    // Кеш для facing direction
    private Direction cachedFacingDirection = null;
    private long cachedFacingTick = -1;

    // Кеш для IFacingComponent - избегаем instanceof проверок и создания итераторов каждый тик
    private IFacingComponent[] cachedFacingInventories = null;
    private IFacingComponent[] cachedFacingTanks = null;
    private boolean facingComponentsCacheValid = false;

    public ActiveTile(BasicTileBlock<T> base, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(base, blockEntityType,pos, state);
        this.guiAddons = new ArrayList<>();
        this.containerAddons = new ArrayList<>();
        this.bundles = new ArrayList<>();
    }

    /**
     * Invalidates the facing components cache. Call this when inventory/tank components change.
     */
    protected void invalidateFacingComponentsCache() {
        this.facingComponentsCacheValid = false;
    }

    private void rebuildFacingComponentsCache() {
        if (facingComponentsCacheValid) return;

        // Кешируем IFacingComponent inventories
        if (multiInventoryComponent != null) {
            List<IFacingComponent> facingInvs = new ArrayList<>();
            for (InventoryComponent<T> inv : multiInventoryComponent.getInventoryHandlers()) {
                if (inv instanceof IFacingComponent fc) {
                    facingInvs.add(fc);
                }
            }
            cachedFacingInventories = facingInvs.isEmpty() ? null : facingInvs.toArray(new IFacingComponent[0]);
        } else {
            cachedFacingInventories = null;
        }

        // Кешируем IFacingComponent tanks
        if (multiTankComponent != null) {
            List<IFacingComponent> facingTanks = new ArrayList<>();
            for (FluidTankComponent<T> tank : multiTankComponent.getTanks()) {
                if (tank instanceof IFacingComponent fc) {
                    facingTanks.add(fc);
                }
            }
            cachedFacingTanks = facingTanks.isEmpty() ? null : facingTanks.toArray(new IFacingComponent[0]);
        } else {
            cachedFacingTanks = null;
        }

        facingComponentsCacheValid = true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public ItemInteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
        if (multiTankComponent != null && FluidUtil.interactWithFluidHandler(player, hand, multiTankComponent.getCapabilityForSide(null).orElse(new MultiTankComponent.MultiTankCapabilityHandler(new ArrayList<>())))) {
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

    }

    public void openGui(Player player) {
        if (player instanceof ServerPlayer sp) {
            sp.openMenu(this, buffer ->
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
        return Component.translatable(getBasicTileBlock().getDescriptionId()).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY));
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

        // Работа с facing компонентами - используем кешированные массивы
        if (level.getGameTime() % getFacingHandlerWorkTime() == 0) {
            rebuildFacingComponentsCache();

            if (cachedFacingInventories != null) {
                Direction facing = this.getFacingDirection();
                int workAmount = getFacingHandlerWorkAmount();
                for (int i = 0; i < cachedFacingInventories.length; i++) {
                    cachedFacingInventories[i].work(this.level, this.worldPosition, facing, workAmount);
                }
            }

            if (cachedFacingTanks != null) {
                Direction facing = this.getFacingDirection();
                int workAmount = getFacingHandlerWorkAmount();
                for (int i = 0; i < cachedFacingTanks.length; i++) {
                    cachedFacingTanks[i].work(this.level, this.worldPosition, facing, workAmount);
                }
            }
        }

        // Обработка отложенного dirty в конце тика - один setChanged() вместо многих
        if (pendingDirty) {
            pendingDirty = false;
            super.setChanged();
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
        // Кеширование facing direction - обновляется раз в тик
        if (this.level != null) {
            long currentTick = this.level.getGameTime();
            if (cachedFacingTick != currentTick || cachedFacingDirection == null) {
                cachedFacingTick = currentTick;
                BlockState state = this.level.getBlockState(worldPosition);
                if (state.hasProperty(RotatableBlock.FACING_ALL)) {
                    cachedFacingDirection = state.getValue(RotatableBlock.FACING_ALL);
                } else if (state.hasProperty(RotatableBlock.FACING_HORIZONTAL)) {
                    cachedFacingDirection = state.getValue(RotatableBlock.FACING_HORIZONTAL);
                } else {
                    cachedFacingDirection = Direction.NORTH;
                }
            }
            return cachedFacingDirection;
        }
        return Direction.NORTH;
    }

    /**
     * Invalidates the cached facing direction. Call this when the block is rotated.
     */
    public void invalidateFacingCache() {
        this.cachedFacingDirection = null;
        this.cachedFacingTick = -1;
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
            if (!compound.contains("Invalid") && compound.contains("Fill") && !playerEntity.containerMenu.getCarried().isEmpty()) {
                boolean fill = compound.getBoolean("Fill");
                String name = compound.getString("Name");
                if (multiTankComponent != null) {
                    for (FluidTankComponent<T> fluidTankComponent : multiTankComponent.getTanks()) {
                        if (fluidTankComponent.getName().equalsIgnoreCase(name))
                            Optional.ofNullable(playerEntity.containerMenu.getCarried().getCapability(Capabilities.FluidHandler.ITEM)).ifPresent(iFluidHandlerItem -> {
                                Item carriedItem = playerEntity.containerMenu.getCarried().getItem();
                                boolean isBucket = carriedItem instanceof BucketItem || carriedItem instanceof MilkBucketItem;

                                if (fill) {
                                    int amount = isBucket ? FluidType.BUCKET_VOLUME : Integer.MAX_VALUE;
                                    amount = fluidTankComponent.fill(iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE);
                                    iFluidHandlerItem.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                                } else {
                                    int amount = isBucket ? FluidType.BUCKET_VOLUME : Integer.MAX_VALUE;
                                    amount = iFluidHandlerItem.fill(fluidTankComponent.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE);
                                    fluidTankComponent.drain(amount, IFluidHandler.FluidAction.EXECUTE);
                                }
                                playerEntity.containerMenu.setCarried(iFluidHandlerItem.getContainer().copy());
                                if (playerEntity instanceof ServerPlayer) {
                                    playerEntity.containerMenu.broadcastChanges();
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
                        filter.setFilter(slot, ItemStack.parseOptional(level.registryAccess(), compound.getCompound("Filter")));
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
                invalidateCapabilities();
                markForUpdate();
            } else if (multiTankComponent != null && multiTankComponent.handleFacingChange(name, facing, faceMode)) {
                invalidateCapabilities();
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
        // Отложенное обновление - setChanged() будет вызван один раз в конце тика
        this.pendingDirty = true;
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

    public boolean canInteract() {
        return this.level.getBlockEntity(this.worldPosition) == this;
    }

    public MultiTankComponent<T> getMultiTankComponent() {
        return multiTankComponent;
    }

    public IFluidHandler getFluidHandler(@Nullable Direction direction) {
        return multiTankComponent == null ? null : multiTankComponent.getCapabilityForSide(FacingUtil.getFacingRelative(getFacingDirection(), direction)).orElse(null);
    }

    public IItemHandler getItemHandler(@Nullable Direction direction) {
        return multiInventoryComponent == null ? null : multiInventoryComponent.getCapabilityForSide(FacingUtil.getFacingRelative(getFacingDirection(), direction)).orElse(null);
    }

    public MultiFilterComponent getMultiFilterComponent() {
        return multiFilterComponent;
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        if (multiInventoryComponent != null) multiInventoryComponent.rebuildCapability(FacingUtil.Sideness.values());
        if (multiTankComponent != null) multiTankComponent.rebuildCapability(FacingUtil.Sideness.values());
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        // Сбрасываем pending dirty перед сохранением, чтобы гарантировать корректное состояние
        this.pendingDirty = false;
        super.saveAdditional(compoundTag, provider);
    }

    @Override
    public void setRemoved() {
        // Flush pending dirty перед удалением tile
        if (pendingDirty && this.level != null && !this.level.isClientSide) {
            pendingDirty = false;
            // Не вызываем setChanged() здесь, т.к. tile уже удаляется
        }
        super.setRemoved();
    }

    @Override
    public void setChanged() {
        // Сбрасываем pending dirty при прямом вызове setChanged()
        this.pendingDirty = false;
        super.setChanged();
    }
}
