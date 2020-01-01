/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.client;

import com.hrznstudio.titanium.api.IFactory;

import java.util.List;

public interface IGuiAddonProvider {

    List<IFactory<? extends IGuiAddon>> getGuiAddons();

}
