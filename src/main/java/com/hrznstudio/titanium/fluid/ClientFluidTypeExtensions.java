/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.fluid;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

public class ClientFluidTypeExtensions implements TitaniumFluidRenderProperties {

    private final Identifier still;
    private final Identifier flow;
    private final @Nullable Identifier overlay;

    public ClientFluidTypeExtensions(Identifier still, Identifier flow) {
        this(still, flow, null);
    }

    public ClientFluidTypeExtensions(Identifier still, Identifier flow, @Nullable Identifier overlay) {
        this.still = still;
        this.flow = flow;
        this.overlay = overlay;
    }

    @Override
    public Identifier getStillTexture() {
        return still;
    }

    @Override
    public Identifier getFlowingTexture() {
        return flow;
    }

    @Override
    public @Nullable Identifier getOverlayTexture() {
        return overlay;
    }

    /**
     * Legacy-compatible tint hook for subclasses that use one color everywhere.
     */
    public int getTintColor() {
        return -1;
    }

    /**
     * Tint hook used for fluid item stacks.
     */
    public int getTintColor(FluidStack stack) {
        return getTintColor();
    }

    /**
     * Tint hook used when the fluid is rendered in the world.
     */
    public int getTintColor(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
        return getTintColor(new FluidStack(fluidState.getType(), 1000));
    }

    @Override
    public FluidTintSource getTintSource() {
        return new FluidTintSource() {
            @Override
            public int color(FluidState state) {
                return getTintColor(new FluidStack(state.getType(), 1000));
            }

            @Override
            public int colorInWorld(FluidState fluidState, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
                return getTintColor(fluidState, blockState, level, pos);
            }

            @Override
            public int colorAsStack(FluidStack stack) {
                return getTintColor(stack);
            }
        };
    }

}
