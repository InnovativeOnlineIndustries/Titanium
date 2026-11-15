/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.capability;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.api.capability.IStackHolder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.ItemCapability;

public class CapabilityItemStackHolder {
    public static final ItemCapability<IStackHolder, Void> ITEMSTACK_HOLDER_CAPABILITY = ItemCapability.createVoid(ResourceLocation.fromNamespaceAndPath(Titanium.MODID, "stackholder"), IStackHolder.class);
}
