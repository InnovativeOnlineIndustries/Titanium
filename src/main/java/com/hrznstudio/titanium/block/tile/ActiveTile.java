/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block.tile;

import com.hrznstudio.titanium.api.IFactory;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;

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

    private List<IFactory<?>> guiAddons;

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
        if (multiTankComponent != null && FluidUtil.interactWithFluidHandler(player, hand, worldPosition, multiTankComponent.getCapabilityForSide(null).orElse(new MultiTankComponent.MultiTankCapabilityHandler(new ArrayList<>())), null)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
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

    public void addGuiAddonFactory(IFactory<?> factory) {
        this.guiAddons.add(factory);
    }

    public void addContainerAddonFactory(IFactory<? extends IContainerAddon> factory) {
        this.containerAddons.add(factory);
    }


    @Override
    public List<IFactory<?>> getScreenAddons() {
        List<IFactory<?>> addons = new ArrayList<>(guiAddons);
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
                    if (inventoryHandler instanceof IFacingComponent)
                        ((IFacingComponent) inventoryHandler).work(this.level, this.worldPosition, this.getFacingDirection(), getFacingHandlerWorkAmount());
                }
            }
            if (multiTankComponent != null) {
                for (FluidTankComponent<T> tank : multiTankComponent.getTanks()) {
                    if (tank instanceof IFacingComponent)
                        ((IFacingComponent) tank).work(this.level, this.worldPosition, this.getFacingDirection(), getFacingHandlerWorkAmount());
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
            if (!compound.contains("Invalid") && compound.contains("Fill") && !playerEntity.containerMenu.getCarried().isEmpty()) {
                boolean fill = compound.getBooleanOr("Fill", false);
                String name = compound.getStringOr("Name", "");
                if (multiTankComponent != null) {
                    for (FluidTankComponent<T> fluidTankComponent : multiTankComponent.getTanks()) {
                        if (fluidTankComponent.getName().equalsIgnoreCase(name)) {
                            ItemStack carriedStack = playerEntity.containerMenu.getCarried();
                            var itemAccess = ItemAccess.forPlayerCursor(playerEntity, playerEntity.containerMenu).oneByOne();
                            Optional.ofNullable(itemAccess.getCapability(Capabilities.Fluid.ITEM)).ifPresent(itemHandler -> {
                                Item carriedItem = carriedStack.getItem();
                                boolean isBucket = carriedItem instanceof BucketItem || carriedItem == Items.MILK_BUCKET;
                                int amount = isBucket ? FluidType.BUCKET_VOLUME : Integer.MAX_VALUE;
                                ResourceHandler<FluidResource> from = fill ? itemHandler : fluidTankComponent;
                                ResourceHandler<FluidResource> to = fill ? fluidTankComponent : itemHandler;
                                ResourceHandlerUtil.move(from, to, resource -> true, amount, null);
                                if (playerEntity instanceof ServerPlayer) {
                                    playerEntity.containerMenu.broadcastChanges();
                                }
                            });
                        }
                    }
                }
            }
        }
        if (id == -2) {
            String name = compound.getStringOr("Name", "");
            if (multiFilterComponent != null) {
                for (IFilter<?> filter : multiFilterComponent.getFilters()) {
                    if (filter.getName().equals(name)) {
                        int slot = compound.getIntOr("Slot", 0);
                        filter.setFilter(slot, com.hrznstudio.titanium.util.ItemStackSerialization.load(level.registryAccess(), compound.getCompoundOrEmpty("Filter")));
                        markComponentDirty();
                        break;
                    }
                }
            }
        }
        if (id == -1) {
            String name = compound.getStringOr("Name", "");
            FacingUtil.Sideness facing = FacingUtil.Sideness.valueOf(compound.getStringOr("Facing", ""));
            int faceMode = compound.getIntOr("Next", 0);
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

    public boolean canInteract() {
        return this.level.getBlockEntity(this.worldPosition) == this;
    }

    public MultiTankComponent<T> getMultiTankComponent() {
        return multiTankComponent;
    }

    public ResourceHandler<FluidResource> getFluidHandler(@Nullable Direction direction) {
        return multiTankComponent == null ? null : multiTankComponent.getCapabilityForSide(FacingUtil.getFacingRelative(getFacingDirection(), direction)).orElse(null);
    }

    public ResourceHandler<ItemResource> getItemHandler(@Nullable Direction direction) {
        return multiInventoryComponent == null ? null : multiInventoryComponent.getCapabilityForSide(FacingUtil.getFacingRelative(getFacingDirection(), direction)).orElse(null);
    }

    public MultiFilterComponent getMultiFilterComponent() {
        return multiFilterComponent;
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        if (multiInventoryComponent != null) multiInventoryComponent.rebuildCapability(FacingUtil.Sideness.values());
        if (multiTankComponent != null) multiTankComponent.rebuildCapability(FacingUtil.Sideness.values());
    }
}
