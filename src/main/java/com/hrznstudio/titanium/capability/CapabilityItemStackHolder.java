/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.api.capability.IStackHolder;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityItemStackHolder {

    @CapabilityInject(IStackHolder.class)
    public static Capability<IStackHolder> ITEMSTACK_HOLDER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IStackHolder.class);
    }

}
