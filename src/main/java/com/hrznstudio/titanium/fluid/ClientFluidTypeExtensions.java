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
