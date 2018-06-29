/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.util;

import com.google.common.collect.Sets;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;

import java.util.Collection;

public class PropertyColor extends PropertyEnum<EnumDyeColor> {
    protected PropertyColor(String name, Class<EnumDyeColor> valueClass, Collection<EnumDyeColor> allowedValues) {
        super(name, valueClass, allowedValues);
    }

    public static PropertyColor create(String name) {
        return new PropertyColor(name, EnumDyeColor.class, Sets.newHashSet(EnumDyeColor.values()));
    }
}
