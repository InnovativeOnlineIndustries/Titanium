/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.network.locator;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PlayerInventoryFinder {

    public static String MAIN = "main_inventory";
    public static HashMap<String, PlayerInventoryFinder> FINDERS = new HashMap<>();

    static {
        FINDERS.put(MAIN, new PlayerInventoryFinder(playerEntity -> playerEntity.getInventory().items.size(), (playerEntity, integer) -> playerEntity.getSlot(integer).get(), (playerEntity, slot, stack) -> playerEntity.getSlot(slot).set(stack)));
    }

    public static Optional<PlayerInventoryFinder> get(String name){
        return Optional.ofNullable(FINDERS.get(name));
    }

    private final Function<Player, Integer> slotAmountGetter;
    private final BiFunction<Player, Integer, ItemStack> stackGetter;
    private final IStackModifier stackSetter;

    public PlayerInventoryFinder(Function<Player, Integer> slotAmountGetter, BiFunction<Player, Integer, ItemStack> stackGetter, IStackModifier stackSetter) {
        this.slotAmountGetter = slotAmountGetter;
        this.stackGetter = stackGetter;
        this.stackSetter = stackSetter;
    }

    public Function<Player, Integer> getSlotAmountGetter() {
        return slotAmountGetter;
    }

    public BiFunction<Player, Integer, ItemStack> getStackGetter() {
        return stackGetter;
    }

    public IStackModifier getStackSetter() {
        return stackSetter;
    }

    public interface IStackModifier{

        void consume(Player playerEntity, int slot, ItemStack stack);

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
