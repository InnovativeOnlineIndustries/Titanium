/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base.api.client;

import com.hrznstudio.titanium.base.api.IFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IGuiAddonProvider {

    @SideOnly(Side.CLIENT)
    List<IFactory<? extends IGuiAddon>> getGuiAddons();

}
