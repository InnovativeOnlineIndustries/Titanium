package com.hrznstudio.titanium.container.addon;

import com.google.common.collect.Lists;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;

public class SlotContainerAddon implements IContainerAddon {
    private final IItemHandler itemHandler;
    private final int xPos;
    private final int yPos;
    private final Function<Integer, Pair<Integer, Integer>> positionFunction;

    public SlotContainerAddon(IItemHandler itemHandler, int xPos, int yPos, Function<Integer, Pair<Integer, Integer>> positionFunction) {
        this.itemHandler = itemHandler;
        this.xPos = xPos;
        this.yPos = yPos;
        this.positionFunction = positionFunction;
    }

    @Override
    public List<Slot> getSlots() {
        List<Slot> slots = Lists.newArrayList();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            Pair<Integer, Integer> offset = positionFunction.apply(i);
            slots.add(new SlotItemHandler(itemHandler, i, xPos + offset.getLeft(), yPos + offset.getRight()));
        }
        return slots;
    }
}
