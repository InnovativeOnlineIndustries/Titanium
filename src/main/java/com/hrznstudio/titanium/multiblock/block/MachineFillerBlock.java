/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.multiblock.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.multiblock.tile.MachineFillerTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.world.gen.feature.template.Template;

public class MachineFillerBlock extends RotatableBlock<MachineFillerTile> {
    private BlockState originalState;
    public static MachineFillerBlock INSTANCE = new MachineFillerBlock();

    public MachineFillerBlock() {
        super(Properties.create(Material.IRON), MachineFillerTile.class);
    }

    public MachineFillerBlock(Properties properties) {
        super(properties, MachineFillerTile.class);
    }

    @Override
    public IFactory getTileEntityFactory() {
        return null;
    }

    public BlockState getOriginalState() {
        return originalState;
    }
}
