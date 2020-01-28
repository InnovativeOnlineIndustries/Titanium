package com.hrznstudio.titanium._impl.test.multiblock;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.multiblock.ShapedMultiblockTemplate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TestMultiblockTemplate extends ShapedMultiblockTemplate {

    public TestMultiblockTemplate() {
        super(new ResourceLocation(Titanium.MODID, "test_multiblock"), BlockPos.ZERO, BlockPos.ZERO);
    }

    @Override
    public boolean canRenderFormedMultiblock() {
        return false;
    }

    @Override
    public void renderFormedMultiblock() {}
}
