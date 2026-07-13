/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.fluid.FluidTintSource;

import javax.annotation.Nullable;

/**
 * Client properties for a Titanium fluid.
 *
 * <p>Fluid model textures and tint are registered through the NeoForge fluid model API, while this interface's
 * {@link IClientFluidTypeExtensions} methods are registered separately for camera overlay and fog behavior.</p>
 */
public interface TitaniumFluidRenderProperties extends IClientFluidTypeExtensions {
    Identifier getStillTexture();

    Identifier getFlowingTexture();

    default @Nullable Identifier getOverlayTexture() {
        return null;
    }

    default @Nullable FluidTintSource getTintSource() {
        return null;
    }
}
