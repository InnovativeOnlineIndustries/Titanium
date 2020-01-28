/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

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
