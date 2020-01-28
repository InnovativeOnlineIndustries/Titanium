package com.hrznstudio.titanium.util;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.hrznstudio.titanium.Titanium.MODID;

public class TitaniumTags {

    public static final Tag<Item> FORMATION_TOOL = new ItemTags.Wrapper(new ResourceLocation(MODID, "formation_tool"));

}
