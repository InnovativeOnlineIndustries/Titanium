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
import com.hrznstudio.titanium.block.BlockResource;
import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.compat.JEICompat;
import com.hrznstudio.titanium.compat.TinkersCompat;
import com.hrznstudio.titanium.item.ItemResource;
import com.hrznstudio.titanium.pulsar.control.PulseManager;
import com.hrznstudio.titanium.tab.AdvancedTitaniumTab;
import com.hrznstudio.titanium.util.SidedHandler;
import com.hrznstudio.titanium.util.TitaniumMod;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;
import java.util.*;

@Mod(Titanium.MODID)
public class Titanium extends TitaniumMod {
    public static final String MODID = "titanium";

    public static List<ItemResource> RESOURCE_ITEMS = new ArrayList<>();
    public static List<BlockResource> RESOURCE_BLOCKS = new ArrayList<>();
    public static AdvancedTitaniumTab RESOURCES_TAB;

    public static Map<ResourceMaterial, Map<ResourceType, Item>> RESOURCE_MAP = new HashMap<>();

    public static PulseManager COMPAT_MANAGER = new PulseManager("titanium/compat");
    private static boolean vanilla;

    static {
        COMPAT_MANAGER.registerPulse(new TinkersCompat());
        COMPAT_MANAGER.registerPulse(new JEICompat());
    }

    public static Optional<Item> getResourceItem(ResourceMaterial material, ResourceType type) {
        if (!RESOURCE_MAP.containsKey(material))
            return Optional.empty();
        return Optional.ofNullable(RESOURCE_MAP.get(material).get(type));
    }

    public static void openGui(TileBase tile, EntityPlayer player) {
        //player.openGui(INSTANCE, -1, tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
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
            ResourceRegistry.addMaterial("diamond", new ResourceMaterial(
                    resourceType -> new ModelResourceLocation(
                            new ResourceLocation(Titanium.MODID, "diamond"),
                            "type=" + resourceType.getName()
                    )
            ).withTypes(ResourceType.VANILLA).withType(ResourceType.INGOT));
        }
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        /*
        for (ResourceType type : ResourceType.values()) {
            if (type.getItemFunction() == null)
                continue;
            ResourceRegistry.getMaterials().forEach(material -> {
                if (material.hasType(type)) {
                    ItemResource item = type.getItemFunction().apply(type, material);
                    event.getRegistry().register(item);
                    RESOURCES_TAB.addIconStacks(item.getStack(1));
                    RESOURCE_ITEMS.add(item);
                    RESOURCE_MAP.computeIfAbsent(material, mat -> new HashMap<>()).put(type, item);

                    OreDictionary.registerOre(item.getType().getOreDict() + StringUtils.capitalize(material.materialName), item.getStack(1));
                }
            });
        }
        if (!RESOURCE_BLOCKS.isEmpty()) {
            RESOURCE_BLOCKS.forEach(block -> {
                Item item = block.getItemBlockFactory().create();
                event.getRegistry().register(item);
                RESOURCE_MAP.computeIfAbsent(block.getResourceMaterial(), mat -> new HashMap<>()).put(block.getType(), item);
                OreDictionary.registerOre(block.getType().getOreDict() + StringUtils.capitalize(block.getResourceMaterial().materialName), item);
            });
        }*/
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        /*
        for (ResourceType type : ResourceType.values()) {
            if (type.getBlockFunction() == null)
                continue;
            ResourceRegistry.getMaterials().forEach(material -> {
                if (material.hasType(type)) {
                    if (RESOURCES_TAB == null)
                        RESOURCES_TAB = new AdvancedTitaniumTab("titanium.resources", true);
                    BlockResource resource = type.getBlockFunction().apply(type,material);
                    resource.setCreativeTab(RESOURCES_TAB);
                    RESOURCES_TAB.addIconStack(new ItemStack(resource));
                    event.getRegistry().register(resource);
                    RESOURCE_BLOCKS.add(resource);
                }
            });
        }*/
    }

    @Override
    @Nonnull
    public String getModId() {
        return MODID;
    }

    public void preInit(FMLPreInitializationEvent event) {
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