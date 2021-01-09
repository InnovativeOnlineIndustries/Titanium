/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium.block.BasicBlock;
import com.hrznstudio.titanium.util.RayTraceUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;

public class TitaniumClient {
    public static void registerModelLoader() {
        //ModelLoaderRegistry.registerLoader(new ResourceLocation(Titanium.MODID, "model_loader"),new TitaniumModelLoader());
    }

    public static PlayerRenderer getPlayerRenderer(Minecraft minecraft) {
        return minecraft.getRenderManager().playerRenderer;
    }

    @OnlyIn(Dist.CLIENT)
    public static void blockOverlayEvent(DrawHighlightEvent.HighlightBlock event) {
        if (event.getTarget() != null) {
            BlockRayTraceResult traceResult = event.getTarget();
            BlockState og = Minecraft.getInstance().world.getBlockState(traceResult.getPos());
            if (og.getBlock() instanceof BasicBlock && ((BasicBlock) og.getBlock()).hasIndividualRenderVoxelShape()) {
                VoxelShape shape = RayTraceUtils.rayTraceVoxelShape(traceResult, Minecraft.getInstance().world, Minecraft.getInstance().player, 32, event.getPartialTicks());
                BlockPos blockpos = event.getTarget().getPos();
                if (shape != null) {
                    event.setCanceled(true);
                    MatrixStack stack = new MatrixStack();
                    stack.push();
                    ActiveRenderInfo info = event.getInfo();
                    stack.rotate(Vector3f.XP.rotationDegrees(info.getPitch()));
                    stack.rotate(Vector3f.YP.rotationDegrees(info.getYaw() + 180));
                    double d0 = info.getProjectedView().getX();
                    double d1 = info.getProjectedView().getY();
                    double d2 = info.getProjectedView().getZ();
                    IVertexBuilder builder = Minecraft.getInstance().getRenderTypeBuffers().getOutlineBufferSource().getBuffer(RenderType.LINES);
                    WorldRenderer.drawBoundingBox(stack, builder, shape.getBoundingBox().offset(blockpos.getX() - d0,
                            blockpos.getY() - d1, blockpos.getZ() - d2), 0, 0, 0, 0.5F);
                    stack.pop();
                }
            }
        }
    }
}
