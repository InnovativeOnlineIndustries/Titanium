/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium.client.TitaniumModelLoader;
import com.hrznstudio.titanium.event.RegisterParticleEvent;
import com.hrznstudio.titanium.particle.ParticleRegistry;
import com.hrznstudio.titanium.particle.ParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

@EventBusSubscriber
public class TitaniumClient {

    public static ParticleRenderer particleRenderer = new ParticleRenderer();

    public static int ticks = 0;

    public static void registerModelLoader() {
        ModelLoaderRegistry.registerLoader(new TitaniumModelLoader());
    }

    public static void registerParticleRegistry() {
        MinecraftForge.EVENT_BUS.post(new RegisterParticleEvent());
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
            TitaniumClient.particleRenderer.updateParticles();
        }
    }

    @SubscribeEvent
    public static void onRenderAfterWorld(RenderWorldLastEvent event) {
        // Renders all particles in Titanium
        GlStateManager.pushMatrix();
        TitaniumClient.particleRenderer.renderParticles(event.getPartialTicks());
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent event) {
        for (Map.Entry<String, ResourceLocation> e : ParticleRegistry.particleTextures.entrySet()) {
            event.getMap().registerSprite(Minecraft.getInstance().getResourceManager(), e.getValue());
        }
    }
}
