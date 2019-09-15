/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator.titanium;

import com.hrznstudio.titanium.block.BlockBase;
import com.hrznstudio.titanium.recipe.generator.TitaniumLootTableProvider;
import net.minecraft.data.DataGenerator;

public class DefaultLootTableProvider extends TitaniumLootTableProvider {

    public DefaultLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    public void add() {
        BlockBase.BLOCKS.forEach(blockBase -> blockBase.createLootTable(this));
    }
}
