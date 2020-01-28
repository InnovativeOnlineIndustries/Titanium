/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.multiblock.IMultiblockComponent;
import com.hrznstudio.titanium.api.multiblock.MultiblockTemplate;
import com.hrznstudio.titanium.multiblock.tile.MultiblockControllerTile;
import com.hrznstudio.titanium.multiblock.tile.MultiblockFillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template;

import java.util.function.Supplier;

public class TitaniumMultiblockTemplate extends MultiblockTemplate {

    private final Supplier<BlockState> blockStateSupplier;

    public TitaniumMultiblockTemplate(ResourceLocation id, BlockPos masterPos, BlockPos triggerPos, Supplier<BlockState> blockStateSupplier) {
        super(id, masterPos, triggerPos);
        this.blockStateSupplier = blockStateSupplier;
    }

    public Supplier<BlockState> getBlockStateSupplier() {
        return blockStateSupplier;
    }
}