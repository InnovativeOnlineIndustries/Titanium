/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class BasicAddonContainer extends BasicInventoryContainer implements IObjectContainer, ILocatable {
    @ObjectHolder("titanium:addon_container")
    public static ContainerType<BasicAddonContainer> TYPE;

    private final IWorldPosCallable worldPosCallable;
    private final Object provider;
    private final LocatorInstance locatorInstance;

    public BasicAddonContainer(Object provider, LocatorInstance locatorInstance, IWorldPosCallable worldPosCallable,
                               PlayerInventory playerInventory, int containerId) {
        this(provider, locatorInstance, TYPE, worldPosCallable, playerInventory, containerId);
    }

    public BasicAddonContainer(Object provider, LocatorInstance locatorInstance, ContainerType<?> containerType,
                               IWorldPosCallable worldPosCallable, PlayerInventory playerInventory, int containerId) {
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
                    containAddon.getIntReferenceHolders().forEach(this::trackInt);
                    containAddon.getIntArrayReferenceHolders().forEach(this::trackIntArray);
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
    public boolean canInteractWith(PlayerEntity playerIn) {
        return !worldPosCallable.applyOrElse((world, blockPos) -> playerIn.getDistanceSq(blockPos.getX() + 0.5D,
            blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D, true) || !(provider instanceof IContainerAddonProvider) || ((IContainerAddonProvider) provider).canInteract();
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (locatorInstance instanceof HeldStackLocatorInstance) {
            if (((HeldStackLocatorInstance) locatorInstance).isMainHand()) {
                if (player.inventory.currentItem == (slotId - 27)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotId == 40) {
                return ItemStack.EMPTY;
            }
        }
        if (locatorInstance instanceof InventoryStackLocatorInstance){
            int slot = ((InventoryStackLocatorInstance) locatorInstance).getInventorySlot();
            if (slot < 9){
                slot += 27;
            } else {
                slot -= 9;
            }
            if (slot == slotId){
                this.detectAndSendChanges();
                return ItemStack.EMPTY;
            }
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    public static BasicAddonContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        LocatorInstance instance = LocatorFactory.readPacketBuffer(packetBuffer);
        if (instance != null) {
            PlayerEntity playerEntity = inventory.player;
            World world = playerEntity.getEntityWorld();
            BasicAddonContainer container = instance.locale(playerEntity)
                .map(located -> new BasicAddonContainer(located, instance, instance.getWorldPosCallable(world),
                    inventory, id))
                .orElse(null);
            if (container != null) {
                return container;
            }
        }
        Titanium.LOGGER.error("Failed to find locate instance to create Container for");
        return new BasicAddonContainer(new Object(), new EmptyLocatorInstance(), IWorldPosCallable.DUMMY, inventory, id);
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

    public void update() {
        this.inventorySlots.stream().filter(slot -> slot instanceof UpdatableSlotItemHandler).forEach(slot -> ((UpdatableSlotItemHandler) slot).update());
    }

}
