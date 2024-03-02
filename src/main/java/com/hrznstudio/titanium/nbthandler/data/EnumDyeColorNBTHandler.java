/*
 * This file is part of Titanium
 * Copyright (C) 2024, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.nbthandler.data;

import net.minecraft.world.item.DyeColor;

public class EnumDyeColorNBTHandler extends EnumNBTHandler<DyeColor> {

    @Override
    public boolean isClassValid(Class<?> aClass) {
        return DyeColor.class.isAssignableFrom(aClass);
    }

    @Override
    protected int getIdFrom(DyeColor obj) {
        return obj.getId();
    }

    @Override
    protected DyeColor getFromId(int id) {
        return DyeColor.byId(id);
    }
}