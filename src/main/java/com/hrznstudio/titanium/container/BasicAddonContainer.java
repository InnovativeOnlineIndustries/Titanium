/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.tile.IScreenInfoProvider;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IHasAssetProvider;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.addon.UpdatableSlotItemHandler;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.network.locator.ILocatable;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.EmptyLocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.HeldStackLocatorInstance;
import com.hrznstudio.titanium.network.locator.instance.InventoryStackLocatorInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;

public class BasicAddonContainer extends BasicInventoryContainer implements IObjectContainer, ILocatable {
    @ObjectHolder(registryName = "minecraft:menu", value = "titanium:addon_container")
    public static MenuType<BasicAddonContainer> TYPE;

    private final ContainerLevelAccess worldPosCallable;
    private final Object provider;
    private final LocatorInstance locatorInstance;

    public BasicAddonContainer(Object provider, LocatorInstance locatorInstance, ContainerLevelAccess worldPosCallable,
                               Inventory playerInventory, int containerId) {
        this(provider, locatorInstance, TYPE, worldPosCallable, playerInventory, containerId);
    }

    public BasicAddonContainer(Object provider, LocatorInstance locatorInstance, MenuType<?> containerType,
                               ContainerLevelAccess worldPosCallable, Inventory playerInventory, int containerId) {
        super(containerType, playerInventory, containerId, findAssetProvider(provider));
        this.worldPosCallable = worldPosCallable;
        this.provider = provider;
        this.locatorInstance = locatorInstance;
        if (this.provider instanceof IContainerAddonProvider) {
            ((IContainerAddonProvider) this.provider).getContainerAddons()
                .stream()
                .map(IFactory::create)
                .forEach(containAddon -> {
                    containAddon.getSlots().forEach(this::addSlot);
                    containAddon.getIntReferenceHolders().forEach(this::addDataSlot);
                    containAddon.getIntArrayReferenceHolders().forEach(this::addDataSlots);
                });
        }
        this.initInventory();
    }

    private static IAssetProvider findAssetProvider(Object provider) {
        if (provider instanceof IHasAssetProvider) {
            return ((IHasAssetProvider) provider).getAssetProvider();
        } else {
            return DefaultAssetProvider.DEFAULT_PROVIDER;
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return !worldPosCallable.evaluate((world, blockPos) -> playerIn.distanceToSqr(blockPos.getX() + 0.5D,
            blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D, true) || !(provider instanceof IContainerAddonProvider) || ((IContainerAddonProvider) provider).canInteract();
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (locatorInstance instanceof InventoryStackLocatorInstance) {
            int slot = ((InventoryStackLocatorInstance) locatorInstance).getInventorySlot();
            if (slot < 9){
                slot += 27;
            } else {
                slot -= 9;
            }
            if (slot == slotId){
                this.broadcastChanges();
                //return ItemStack.EMPTY;
            }
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    public static BasicAddonContainer create(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        LocatorInstance instance = LocatorFactory.readPacketBuffer(packetBuffer);
        if (instance != null) {
            Player playerEntity = inventory.player;
            Level world = playerEntity.getCommandSenderWorld();
            BasicAddonContainer container = instance.locale(playerEntity)
                .map(located -> new BasicAddonContainer(located, instance, instance.getWorldPosCallable(world),
                    inventory, id))
                .orElse(null);
            if (container != null) {
                return container;
            }
        }
        Titanium.LOGGER.error("Failed to find locate instance to create Container for");
        return new BasicAddonContainer(new Object(), new EmptyLocatorInstance(), ContainerLevelAccess.NULL, inventory, id);
    }

    public Object getProvider() {
        return provider;
    }

    @Override
    public Object getObject() {
        return this.getProvider();
    }

    @Override
    public LocatorInstance getLocatorInstance() {
        return locatorInstance;
    }

    public int getTitleColorFromProvider() {
        if (this.provider instanceof IScreenInfoProvider provider) return provider.getTitleColor();
        return 0xFFFFFF;
    }

    public void update() {
        this.slots.stream().filter(slot -> slot instanceof UpdatableSlotItemHandler).forEach(slot -> ((UpdatableSlotItemHandler) slot).update());
    }

}
