/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium;

import com.hrznstudio.titanium.api.raytrace.DistanceRayTraceResult;
import com.hrznstudio.titanium.api.resource.ResourceMaterial;
import com.hrznstudio.titanium.api.resource.ResourceRegistry;
import com.hrznstudio.titanium.api.resource.ResourceType;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.client.gui.GuiHandler;
import com.hrznstudio.titanium.compat.TinkersCompat;
import com.hrznstudio.titanium.item.ItemBase;
import com.hrznstudio.titanium.item.ItemResource;
import com.hrznstudio.titanium.pulsar.control.PulseManager;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.SidedHandler;
import com.hrznstudio.titanium.util.TitaniumMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Titanium.MODID, name = Titanium.NAME, version = Titanium.VERSION)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";
    public static final String NAME = "Titanium";
    public static final String VERSION = "1.0.0";

    @Mod.Instance
    public static Titanium INSTANCE;
    public static List<ItemResource> RESOURCE_ITEMS = new ArrayList<>();
    public static AdvancedTitaniumTab RESOURCES_TAB;
    private static PulseManager COMPAT_MANAGER = new PulseManager("titanium/compat");
    private static boolean vanilla;

    static {
        COMPAT_MANAGER.registerPulse(new TinkersCompat());
    }

    public static void openGui(TileBase tile, EntityPlayer player) {
        player.openGui(INSTANCE, 0, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
    }

    public static void registerVanillaMaterials() {
        if (!vanilla) {
            vanilla = true;
            ResourceRegistry.addMaterial("iron", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "iron"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
            ResourceRegistry.addMaterial("gold", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "gold"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
            ResourceRegistry.addMaterial("lapis", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "lapis"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA));
            ResourceRegistry.addMaterial("obsidian", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "obsidian"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA).withType(ResourceType.INGOT));
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        for (ResourceType type : ResourceType.values()) {
            if (type.hasMaterial()) {
                RESOURCE_ITEMS.add(new ItemResource(type));
            }
        }
        if (!RESOURCE_ITEMS.isEmpty()) {
            RESOURCES_TAB = new AdvancedTitaniumTab("titanium.resources", true);
            RESOURCE_ITEMS.forEach(item -> {
                event.getRegistry().register(item.setCreativeTab(RESOURCES_TAB));
                ResourceRegistry.getMaterials().forEach(material -> {
                    if (material.hasType(item.getType())) {
                        RESOURCES_TAB.addIconStacks(item.getStack(material, 1));
                        OreDictionary.registerOre(item.getType().getOreDict() + StringUtils.capitalize(material.materialName), item.getStack(material, 1));
                    }
                });
            });
        }
    }

    @Override
    @Nonnull
    public String getModId() {
        return MODID;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SidedHandler.runOn(Side.CLIENT, () -> TitaniumClient::registerModelLoader);
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
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