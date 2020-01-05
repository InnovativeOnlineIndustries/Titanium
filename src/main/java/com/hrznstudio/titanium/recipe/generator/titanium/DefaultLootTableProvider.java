/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.recipe.generator.titanium;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.recipe.generator.TitaniumLootTableProvider;
import net.minecraft.data.DataGenerator;

import java.util.Optional;

public class DefaultLootTableProvider extends TitaniumLootTableProvider {

    private final String modid;

    public DefaultLootTableProvider(DataGenerator dataGeneratorIn, String modid) {
        super(dataGeneratorIn);
        this.modid = modid;
    }

    @Override
    public void add() {
        BasicBlock.BLOCKS.stream()
                .filter(basicBlock -> Optional.ofNullable(basicBlock.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(modid))
                        .isPresent())
                .forEach(basicBlock -> basicBlock.createLootTable(this));
    }
}
