/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.container.addon;

import com.hrznstudio.titanium.api.IFactory;

import javax.annotation.Nonnull;
import java.util.List;

public interface IContainerAddonProvider {
    @Nonnull
    List<IFactory<? extends IContainerAddon>> getContainerAddons();
}
