package com.hrznstudio.titanium.container.addon;

import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.lang3.tuple.Pair;

public class UpdatableSlotItemHandler extends SlotItemHandler {

    private boolean enabled;

    public UpdatableSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.enabled = true;
    }

    public void update() {
        if (this.getItemHandler() instanceof InventoryComponent) {
            Pair<Integer, Integer> pos = ((InventoryComponent<?>) this.getItemHandler()).getSlotPosition().apply(this.getSlotIndex());
            this.xPos = ((InventoryComponent<?>) this.getItemHandler()).getXPos() + pos.getLeft();
            this.yPos = ((InventoryComponent<?>) this.getItemHandler()).getYPos() + pos.getRight();
            this.enabled = ((InventoryComponent<?>) this.getItemHandler()).getSlotEnabled().test(this.getSlotIndex());
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
