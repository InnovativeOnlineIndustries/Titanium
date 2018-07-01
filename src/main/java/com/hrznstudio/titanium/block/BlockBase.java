/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.api.internal.IItemBlockFactory;
import com.hrznstudio.titanium.api.internal.IModelRegistrar;
import com.hrznstudio.titanium.util.ClientUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

public abstract class BlockBase extends Block implements IModelRegistrar, IItemBlockFactory {
    public BlockBase(String name, Material materialIn) {
        super(materialIn);
        setRegistryName(name);
        setUnlocalizedName(Objects.requireNonNull(getRegistryName()).toString().replace(':', '.'));
    }

    @Override
    public IFactory<ItemBlock> getItemBlockFactory() {
        return () -> (ItemBlock) new ItemBlock(this).setRegistryName(Objects.requireNonNull(getRegistryName()));
    }

    @Override
    public void registerModels() {
        ClientUtil.registerToMapper(this);
    }
}
