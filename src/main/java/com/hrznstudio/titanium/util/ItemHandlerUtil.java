package com.hrznstudio.titanium.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class ItemHandlerUtil {

    @Nonnull
    public static ItemStack getFirstItem(IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) {
                return handler.getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }

}
