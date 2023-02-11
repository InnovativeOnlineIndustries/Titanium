/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium._impl;

import com.hrznstudio.titanium.annotation.config.ConfigFile;
import com.hrznstudio.titanium.annotation.config.ConfigVal;

import java.util.Arrays;
import java.util.List;

@ConfigFile(value = "titanium-tags")
public class TagConfig {

    @ConfigVal(comment = "A list of mod ids sorted by preference when getting an Item for a tag")
    public static List<String> ITEM_PREFERENCE = Arrays.asList("minecraft" , "emendatusenigmatica", "immersiveengineering", "thermal", "create", "mekanism", "jaopca", "kubejs", "appliedenergistics2", "pneumaticcraft", "occultism", "tmechworks", "industrialforegoing", "botania", "quark", "pedestals");

}
