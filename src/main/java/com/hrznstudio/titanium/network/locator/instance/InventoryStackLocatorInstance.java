package com.hrznstudio.titanium.network.locator.instance;

import com.hrznstudio.titanium.itemstack.ItemStackHarnessRegistry;
import com.hrznstudio.titanium.network.locator.LocatorInstance;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class InventoryStackLocatorInstance extends LocatorInstance {

    private int inventorySlot;

    public InventoryStackLocatorInstance() {
        this(0);
    }

    public InventoryStackLocatorInstance(int inventorySlot) {
        super(LocatorTypes.INVENTORY_LOCATOR);
        this.inventorySlot = inventorySlot;
    }

    @Override
    public Optional<?> locale(PlayerEntity playerEntity) {
        return Optional.of(playerEntity.inventory.getStackInSlot(inventorySlot))
                .map(ItemStackHarnessRegistry::createItemStackHarness).orElseGet(null);
    }

    public int getInventorySlot() {
        return inventorySlot;
    }
}
