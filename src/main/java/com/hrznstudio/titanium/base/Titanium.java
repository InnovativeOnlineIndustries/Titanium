/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 */
package com.hrznstudio.titanium.base;

import com.hrznstudio.titanium._test.BlockTest;
import com.hrznstudio.titanium.base.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.base.block.tile.TileBase;
import com.hrznstudio.titanium.base.item.ItemBase;
import com.hrznstudio.titanium.base.pulsar.control.PulseManager;
import com.hrznstudio.titanium.base.util.SidedHandler;
import com.hrznstudio.titanium.base.util.TitaniumMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;

@Mod(modid = Titanium.MODID, name = Titanium.NAME, version = Titanium.VERSION)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";
    public static final String NAME = "Titanium";
    public static final String VERSION = "1.0.0";

    public static Titanium INSTANCE;
    public static BlockTest TEST;

    private static PulseManager PULSE_MANAGER = new PulseManager("titanium/modules");
    private static PulseManager MOD_COMPAT = new PulseManager("titanium/compat");

    public static String[] MODULES = new String[]{
            "base",
            "commodore",
            "cassandra",
            "corporis",
            "perplex",
            "nucleus",
    };

    static {
        for (String module : MODULES) {
            try {
                Class<?> clazz = Class.forName(String.format("com.hrznstudio.titanium.%s.%s", module, StringUtils.capitalize(module)));
                try {
                    PULSE_MANAGER.registerPulse(clazz.getConstructor(PulseManager.class).newInstance(MOD_COMPAT));
                } catch (NoSuchMethodException ex) {
                    PULSE_MANAGER.registerPulse(clazz.getConstructor().newInstance());
                }
            } catch (ClassNotFoundException e) {
                // Not Found, Not an error!
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void openGui(TileBase tile, EntityPlayer player) {
        player.openGui(INSTANCE, 0, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        INSTANCE = this;
        SidedHandler.runOn(Side.CLIENT, () -> TitaniumClient::registerModelLoader);
        //TEST
        addBlocks(new BlockTest());
        PULSE_MANAGER.propagateEvent(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PULSE_MANAGER.propagateEvent(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PULSE_MANAGER.propagateEvent(event);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void modelRegistry(ModelRegistryEvent event) {
        ItemBase.ITEMS.forEach(ItemBase::registerModels);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = event.getTarget().getBlockPos();
        RayTraceResult hit = event.getTarget();
        if (hit.typeOfHit == RayTraceResult.Type.BLOCK && hit instanceof DistanceRayTraceResult) {
            event.setCanceled(true);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth(2.0F);
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
