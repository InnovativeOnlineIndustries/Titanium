/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public abstract class BlockBase extends Block {
    public BlockBase(String name, Material materialIn) {
        super(materialIn);
        setRegistryName(name);
        setUnlocalizedName(Objects.requireNonNull(getRegistryName()).toString().replace(':', '.'));
    }

    public IFactory<ItemBlock> getItemBlockFactory() {
        return () -> (ItemBlock) new ItemBlock(this).setRegistryName(Objects.requireNonNull(getRegistryName()));
    }
}
