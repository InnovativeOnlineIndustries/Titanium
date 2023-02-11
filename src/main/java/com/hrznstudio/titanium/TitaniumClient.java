/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.util.RayTraceUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHighlightEvent;

public class TitaniumClient {
    public static void registerModelLoader() {
        //ModelLoaderRegistry.registerLoader(new ResourceLocation(Titanium.MODID, "model_loader"),new TitaniumModelLoader());
    }

    public static EntityRenderer<? super AbstractClientPlayer> getPlayerRenderer(Minecraft minecraft, AbstractClientPlayer player) {
        return minecraft.getEntityRenderDispatcher().getRenderer(player);
    }

    @OnlyIn(Dist.CLIENT)
    public static void blockOverlayEvent(RenderHighlightEvent.Block event) {
        if (event.getTarget() != null) {
            BlockHitResult traceResult = event.getTarget();
            BlockState og = Minecraft.getInstance().level.getBlockState(traceResult.getBlockPos());
            if (og.getBlock() instanceof BasicBlock && ((BasicBlock) og.getBlock()).hasIndividualRenderVoxelShape()) {
                VoxelShape shape = RayTraceUtils.rayTraceVoxelShape(traceResult, Minecraft.getInstance().level, Minecraft.getInstance().player, 32, event.getPartialTick());
                BlockPos blockpos = event.getTarget().getBlockPos();
                event.setCanceled(true);
                if (shape != null && !shape.isEmpty()) {
                    PoseStack stack = new PoseStack();
                    stack.pushPose();
                    Camera info = event.getCamera();
                    stack.mulPose(Vector3f.XP.rotationDegrees(info.getXRot()));
                    stack.mulPose(Vector3f.YP.rotationDegrees(info.getYRot() + 180));
                    double d0 = info.getPosition().x();
                    double d1 = info.getPosition().y();
                    double d2 = info.getPosition().z();
                    VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.LINES);
                    drawShape(stack, builder, shape, blockpos.getX() - d0,
                        blockpos.getY() - d1, blockpos.getZ() - d2,0, 0, 0, 0.5F);
                    stack.popPose();
                }
            }
        }
    }

    private static void drawShape(PoseStack matrixStackIn, VertexConsumer bufferIn, VoxelShape shapeIn, double xIn, double yIn, double zIn, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrixStackIn.last().pose();
        PoseStack.Pose posestack$pose = matrixStackIn.last();
        shapeIn.forAllEdges((p_230013_12_, p_230013_14_, p_230013_16_, p_230013_18_, p_230013_20_, p_230013_22_) -> {
            float f = (float) (p_230013_18_ - p_230013_12_);
            float f1 = (float) (p_230013_20_ - p_230013_14_);
            float f2 = (float) (p_230013_22_ - p_230013_16_);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f = f / f3;
            f1 = f1 / f3;
            f2 = f2 / f3;
            bufferIn.vertex(matrix4f, (float) (p_230013_12_ + xIn), (float) (p_230013_14_ + yIn), (float) (p_230013_16_ + zIn)).color(red, green, blue, alpha).normal(posestack$pose.normal(), f, f1, f2).endVertex();
            bufferIn.vertex(matrix4f, (float) (p_230013_18_ + xIn), (float) (p_230013_20_ + yIn), (float) (p_230013_22_ + zIn)).color(red, green, blue, alpha).normal(posestack$pose.normal(), f, f1, f2).endVertex();
        });
    }
}
