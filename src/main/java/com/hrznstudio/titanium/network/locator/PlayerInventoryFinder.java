/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PlayerInventoryFinder {

    public static String MAIN = "main_inventory";
    public static HashMap<String, PlayerInventoryFinder> FINDERS = new HashMap<>();

    static {
        FINDERS.put(MAIN, new PlayerInventoryFinder(playerEntity -> playerEntity.inventory.mainInventory.size(), (playerEntity, integer) -> playerEntity.inventory.mainInventory.get(integer), (playerEntity, slot, stack) -> playerEntity.inventory.mainInventory.set(slot, stack)));
    }

    public static Optional<PlayerInventoryFinder> get(String name){
        return Optional.ofNullable(FINDERS.get(name));
    }

    private final Function<PlayerEntity, Integer> slotAmountGetter;
    private final BiFunction<PlayerEntity, Integer, ItemStack> stackGetter;
    private final IStackModifier stackSetter;

    public PlayerInventoryFinder(Function<PlayerEntity, Integer> slotAmountGetter, BiFunction<PlayerEntity, Integer, ItemStack> stackGetter, IStackModifier stackSetter) {
        this.slotAmountGetter = slotAmountGetter;
        this.stackGetter = stackGetter;
        this.stackSetter = stackSetter;
    }

    public Function<PlayerEntity, Integer> getSlotAmountGetter() {
        return slotAmountGetter;
    }

    public BiFunction<PlayerEntity, Integer, ItemStack> getStackGetter() {
        return stackGetter;
    }

    public IStackModifier getStackSetter() {
        return stackSetter;
    }

    public interface IStackModifier{

        void consume(PlayerEntity playerEntity, int slot, ItemStack stack);

    }

    public static class Target{

        private final String name;
        private final PlayerInventoryFinder finder;
        private final int slot;

        public Target(String name, PlayerInventoryFinder finder, int slot) {
            this.name = name;
            this.finder = finder;
            this.slot = slot;
        }

        public String getName() {
            return name;
        }

        public PlayerInventoryFinder getFinder() {
            return finder;
        }

        public int getSlot() {
            return slot;
        }
    }

    public static void init(){};

}
