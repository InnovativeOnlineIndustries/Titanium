/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class ClientFluidTypeExtensions implements IClientFluidTypeExtensions {

    private final ResourceLocation still;
    private final ResourceLocation flow;

    public ClientFluidTypeExtensions(ResourceLocation still, ResourceLocation flow) {
        this.still = still;
        this.flow = flow;
    }

    @Override
    public ResourceLocation getStillTexture() {
        return still;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return flow;
    }


}
