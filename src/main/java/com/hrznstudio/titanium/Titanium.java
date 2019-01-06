/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium;

import com.hrznstudio.titanium._test.BlockTest;
import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.pulsar.control.PulseManager;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.SidedHandler;
import com.hrznstudio.titanium.util.TitaniumMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

@Mod(Titanium.MODID)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";

    public static AdvancedTitaniumTab RESOURCES_TAB;

    public static PulseManager COMPAT_MANAGER = null;
    private static boolean vanilla;

    public static void openGui(TileBase tile, EntityPlayer player) {
        //player.openGui(INSTANCE, -1, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
    }

    public Titanium() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventReceiver
    public void preInit(FMLPreInitializationEvent event) {
        COMPAT_MANAGER = new PulseManager("titanium/compat");
        addBlock(BlockTest.TEST = new BlockTest());
        SidedHandler.runOn(Dist.CLIENT, () -> TitaniumClient::registerModelLoader);
        /*
        if (Loader.isModLoaded("mcmultipart"))
            NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new MCMPGuiHandler());
        else
            NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());*/
    }

    @SubscribeEvent
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = event.getTarget().getBlockPos();
        RayTraceResult hit = event.getTarget();
        if (hit.type == RayTraceResult.Type.BLOCK && hit instanceof DistanceRayTraceResult) {
            event.setCanceled(true);
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.lineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            EntityPlayer player = event.getPlayer();
            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();
            RenderGlobal.drawSelectionBoundingBox(((DistanceRayTraceResult) hit).getHitBox().offset(-x, -y, -z).offset(pos).grow(0.002), 0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }
}