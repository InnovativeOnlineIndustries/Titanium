package com.hrznstudio.titanium.block.tile.container;

import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.block.tile.container.capability.items.PosInvHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerTileBase<T extends TileBase> extends Container {

    private T tile;
    private InventoryPlayer player;
    private boolean hasPlayerInventory;

    public ContainerTileBase(T tile, InventoryPlayer player) {
        this.tile = tile;
        this.player = player;
        for (PosInvHandler handler : tile.getMultiInventoryHandler().getInventoryHandlers()) {
            int i = 0;
            for (int y = 0; y < handler.getYSize(); ++y) {
                for (int x = 0; x < handler.getXSize(); ++x) {
                    addSlotToContainer(new SlotItemHandler(handler, i, handler.getXPos() + x * 18, handler.getYPos() + y * 18));
                    ++i;
                }
            }
        }
        addPlayerChestInventory();
    }

    public void addPlayerChestInventory() {
        if (hasPlayerInventory) return;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 18 + 84 + i * 18));
            }
        }
        hasPlayerInventory = true;
    }

    public void removeChestInventory() {
        this.inventorySlots.removeIf(slot -> slot.getSlotIndex() >= 9 && slot.getSlotIndex() < 9 + 3 * 9);
        hasPlayerInventory = false;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
