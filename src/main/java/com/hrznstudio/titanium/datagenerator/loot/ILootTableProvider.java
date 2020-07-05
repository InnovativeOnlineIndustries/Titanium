package com.hrznstudio.titanium.datagenerator.loot;

import net.minecraft.loot.LootTable;

public interface ILootTableProvider<T> {
    LootTable.Builder getLootTableBuilder(T helper);
}
