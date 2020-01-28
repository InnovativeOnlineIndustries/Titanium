package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.multiblock.block.MultiblockFillerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class TestMultiblockFillerBlock extends MultiblockFillerBlock<TestMultiblockFillerTile> {

    public static TestMultiblockFillerBlock TEST;

    public TestMultiblockFillerBlock() {
        super(Block.Properties.create(Material.IRON), TestMultiblockFillerTile.class);
        setRegistryName(new ResourceLocation(Titanium.MODID, "test_filler_block"));
    }

    @Override
    public IFactory<TestMultiblockFillerTile> getTileEntityFactory() {
        return TestMultiblockFillerTile::new;
    }
}
