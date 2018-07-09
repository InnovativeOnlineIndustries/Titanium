/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.util;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.Optional;

public class CapabilityHandler {
    public static <T> Optional<T> getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, @Nullable EnumFacing facing) {
        return provider == null ? Optional.empty() : Optional.ofNullable(provider.getCapability(capability, facing));
    }

    public static <T> Optional<T> getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability) {
        return getCapability(provider, capability, null);
    }
}