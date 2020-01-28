package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.multiblock.block.MultiblockControllerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class TestMultiblockControllerBlock extends MultiblockControllerBlock<TestMultiblockControllerTile> {

    public static TestMultiblockControllerBlock TEST;

    public TestMultiblockControllerBlock() {
        super(Block.Properties.create(Material.IRON), TestMultiblockControllerTile.class);
        setRegistryName(new ResourceLocation(Titanium.MODID, "test_controller_block"));
    }

    @Override
    public IFactory<TestMultiblockControllerTile> getTileEntityFactory() {
        return TestMultiblockControllerTile::new;
    }
}
