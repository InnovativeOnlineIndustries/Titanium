/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParticleRenderer {

    private CopyOnWriteArrayList<Particle> particles = new CopyOnWriteArrayList<>();

    public synchronized void updateParticles() {
        List<Particle> toRemove = new ArrayList<>();

        particles.forEach(particle -> {
            if (particle instanceof IParticle && ((IParticle) particle).alive()) {
                particle.tick();
            } else {
                toRemove.add(particle);
            }
        });

        if (!toRemove.isEmpty()) {
            particles.removeAll(toRemove);
        }
    }

    public synchronized void renderParticles(float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        EntityPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
            Particle.cameraViewDir = player.getLook(partialTicks);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.alphaFunc(516, 0.003921569F);
            GlStateManager.disableCull();

            GlStateManager.depthMask(false);

            Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            particles.stream()
                    .filter(particle -> particle instanceof IParticle && !((IParticle) particle).isAdditive())
                    .forEach(particle -> particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3));
            tess.draw();

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            particles.stream()
                    .filter(particle -> particle instanceof IParticle && ((IParticle) particle).isAdditive())
                    .forEach(particle -> particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3));
            tess.draw();

            GlStateManager.disableDepthTest();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            particles.stream()
                    .filter(particle -> particle instanceof IParticle && !((IParticle) particle).isAdditive() && ((IParticle) particle).renderThroughBlocks())
                    .forEach(particle -> particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3));
            tess.draw();

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            particles.stream()
                    .filter(particle -> particle instanceof IParticle && ((IParticle) particle).isAdditive() && ((IParticle) particle).renderThroughBlocks())
                    .forEach(particle -> particle.renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3));
            tess.draw();
            GlStateManager.enableDepthTest();

            GlStateManager.enableCull();
            GlStateManager.depthMask(true);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableAlphaTest();
        }
    }

    public void spawnParticle(World world, String particle, double x, double y, double z, double vx, double vy, double vz, double... data) {
        if (world.isRemote) {
            try {
                particles.add(ParticleRegistry.getParticles().get(particle).newInstance(world, x, y, z, vx, vy, vz, data));
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
