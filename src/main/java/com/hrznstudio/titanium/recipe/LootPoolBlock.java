/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe;

import com.hrznstudio.titanium.block.BlockBase;
import com.hrznstudio.titanium.recipe.generator.IJsonFile;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class LootPoolBlock implements IJsonFile {

    private final String type = "minecraft:block";
    private BlockBase.Pool[] pools;
    private transient String key;

    public LootPoolBlock(ResourceLocation location, List<BlockBase.Pool> pools) {
        this.key = location.getNamespace() + "_" + location.getPath();
        this.pools = pools.isEmpty() ? null : pools.toArray(new BlockBase.Pool[]{});
    }

    @Override
    public String getRecipeKey() {
        return key;
    }
}
