/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.hrznstudio.titanium.network.locator.LocatorFactory;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.registries.ObjectHolder;

public class BasicAddonContainer extends BasicInventoryContainer implements IObjectContainer {
    @ObjectHolder("titanium:addon_container")
    public static ContainerType<BasicAddonContainer> TYPE;

    private final IWorldPosCallable worldPosCallable;
    private final Object provider;

    public BasicAddonContainer(Object provider, IWorldPosCallable worldPosCallable,
                               PlayerInventory playerInventory, int containerId) {
        this(provider, TYPE, worldPosCallable, playerInventory, containerId);
    }

    public BasicAddonContainer(Object provider, ContainerType<?> containerType, IWorldPosCallable worldPosCallable,
                               PlayerInventory playerInventory, int containerId) {
        super(containerType, playerInventory, containerId);
        this.worldPosCallable = worldPosCallable;
        this.provider = provider;
        if (provider instanceof IContainerAddonProvider) {
            ((IContainerAddonProvider) provider).getContainerAddons()
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

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return worldPosCallable.applyOrElse((world, blockPos) -> playerIn.getDistanceSq(blockPos.getX() + 0.5D,
                blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D, true);
    }

    public static BasicAddonContainer create(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        LocatorInstance instance = LocatorFactory.readPacketBuffer(packetBuffer);
        if (instance != null) {
            PlayerEntity playerEntity = inventory.player;
            BasicAddonContainer container = instance.locale(playerEntity)
                    .map(located -> new BasicAddonContainer(located, instance.getWorldPosCallable(playerEntity.world),
                            inventory, id))
                    .orElse(null);
            if (container != null) {
                return container;
            }
        }
        throw new IllegalStateException("Failed to Locate Object for Container");
    }

    public Object getProvider() {
        return provider;
    }

    @Override
    public Object getObject() {
        return this.getProvider();
    }
}
