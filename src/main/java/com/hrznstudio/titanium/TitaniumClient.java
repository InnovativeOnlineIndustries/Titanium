/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.util.RayTraceUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

@Mod(value = Titanium.MODID, dist = Dist.CLIENT)
public class TitaniumClient {
    public static void registerModelLoader() {
        //ModelLoaderRegistry.registerLoader(new ResourceLocation(Titanium.MODID, "model_loader"),new TitaniumModelLoader());
    }

    public TitaniumClient(IEventBus bus) {
        bus.addListener((final RegisterMenuScreensEvent event) -> {
            event.register((MenuType<? extends BasicAddonContainer>) BasicAddonContainer.TYPE.get(), BasicAddonScreen::new);
        });
    }

    public static EntityRenderer<? super AbstractClientPlayer> getPlayerRenderer(Minecraft minecraft, AbstractClientPlayer player) {
        return minecraft.getEntityRenderDispatcher().getRenderer(player);
    }

    @OnlyIn(Dist.CLIENT)
    public static void blockOverlayEvent(RenderHighlightEvent.Block event) {
        BlockHitResult traceResult = event.getTarget();
        BlockState og = Minecraft.getInstance().level.getBlockState(traceResult.getBlockPos());
        if (og.getBlock() instanceof BasicBlock && ((BasicBlock) og.getBlock()).hasIndividualRenderVoxelShape()) {
            VoxelShape shape = RayTraceUtils.rayTraceVoxelShape(traceResult, Minecraft.getInstance().level, Minecraft.getInstance().player, 32, event.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            BlockPos blockpos = event.getTarget().getBlockPos();
            event.setCanceled(true);
            if (shape != null && !shape.isEmpty()) {
                PoseStack stack = new PoseStack();
                stack.pushPose();
                Camera info = event.getCamera();
                double d0 = info.getPosition().x();
                double d1 = info.getPosition().y();
                double d2 = info.getPosition().z();
                VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.LINES);
                LevelRenderer.renderShape(stack, builder, shape, blockpos.getX() - d0,
                    blockpos.getY() - d1, blockpos.getZ() - d2, 0, 0, 0, 0.5F);
                stack.popPose();
            }
        }
    }
}
