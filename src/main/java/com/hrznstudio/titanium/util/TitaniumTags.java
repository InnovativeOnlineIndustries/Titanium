/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.util;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.hrznstudio.titanium.Titanium.MODID;

public class TitaniumTags {

    public static final Tag<Item> FORMATION_TOOL = new ItemTags.Wrapper(new ResourceLocation(MODID, "formation_tool"));

}
