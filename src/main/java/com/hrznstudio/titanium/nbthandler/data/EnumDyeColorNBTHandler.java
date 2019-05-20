/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import net.minecraft.item.EnumDyeColor;

public class EnumDyeColorNBTHandler extends EnumNBTHandler<EnumDyeColor> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return EnumDyeColor.class.isAssignableFrom(aClass);
    }

    @Override
    protected int getIdFrom(EnumDyeColor obj) {
        return obj.getId();
    }

    @Override
    protected EnumDyeColor getFromId(int id) {
        return EnumDyeColor.byId(id);
    }
}