/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium.block;

import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import net.minecraft.block.material.Material;

public class BlockOre extends BlockBase {
    private ResourceMaterial material;
    public BlockOre(ResourceMaterial material) {
        super("ore_" + material.materialName, Material.ROCK);
        this.material=material;
    }
}