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
