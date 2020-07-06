/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.component;

import com.hrznstudio.titanium.api.client.IScreenAddonProvider;
import com.hrznstudio.titanium.container.addon.IContainerAddonProvider;

public interface IComponentBundle extends IScreenAddonProvider, IContainerAddonProvider {

    void accept(IComponentHandler<?>... handler);

}
