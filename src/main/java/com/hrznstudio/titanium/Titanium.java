/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium;

import com.hrznstudio.titanium._impl.creative.CreativeFEGeneratorBlock;
import com.hrznstudio.titanium._impl.test.AssetTestBlock;
import com.hrznstudio.titanium._impl.test.MachineTestBlock;
import com.hrznstudio.titanium._impl.test.TestBlock;
import com.hrznstudio.titanium._impl.test.TwentyFourTestBlock;
import com.hrznstudio.titanium._impl.test.multiblock.TestMultiblockControllerBlock;
import com.hrznstudio.titanium._impl.test.multiblock.TestMultiblockFillerBlock;
import com.hrznstudio.titanium._impl.test.recipe.TestSerializableRecipe;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.client.screen.container.BasicTileContainerScreen;
import com.hrznstudio.titanium.command.RewardCommand;
import com.hrznstudio.titanium.command.RewardGrantCommand;
import com.hrznstudio.titanium.container.impl.BasicTileContainer;
import com.hrznstudio.titanium.event.custom.ResourceRegistrationEvent;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.material.ResourceType;
import com.hrznstudio.titanium.material.ResourceTypeProperties;
import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.recipe.generator.BlockItemModelGeneratorProvider;
import com.hrznstudio.titanium.recipe.generator.titanium.DefaultLootTableProvider;
import com.hrznstudio.titanium.recipe.generator.titanium.JsonRecipeSerializerProvider;
import com.hrznstudio.titanium.recipe.generator.titanium.ResourceRegistryProvider;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.RewardSyncMessage;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.hrznstudio.titanium.util.SidedHandlerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


@Mod(Titanium.MODID)
public class Titanium extends ModuleController {

    public static final String MODID = "titanium";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static NetworkHandler NETWORK = new NetworkHandler(MODID);

    public Titanium() {
        NETWORK.registerMessage(ButtonClickNetworkMessage.class);
        NETWORK.registerMessage(RewardSyncMessage.class);
        SidedHandlerUtil.runOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(this::clientSetup).subscribe());
        EventManager.mod(FMLCommonSetupEvent.class).process(this::commonSetup).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class).process(this::onPlayerLoggedIn).subscribe();
        EventManager.forge(FMLServerStartingEvent.class).process(this::onServerStart).subscribe();
    }

    public static void openGui(ActiveTile tile, ServerPlayerEntity player) {
        NetworkHooks.openGui(player, tile, tile.getPos());
    }

    @Override
    public void onPreInit() {
        super.onPreInit();
        EventManager.mod(ResourceRegistrationEvent.class).process(event -> {
            ResourceTypeProperties.DEFAULTS.put(Block.class, new ResourceTypeProperties(Block.Properties.from(Blocks.IRON_ORE)));
            ResourceTypeProperties.DEFAULTS.put(Item.class, new ResourceTypeProperties(new Item.Properties().group(ResourceRegistry.RESOURCES)));
            event.get("iron").setColor(0xd8d8d8).withOverride(ResourceType.ORE, Blocks.IRON_ORE).withOverride(ResourceType.METAL_BLOCK, Blocks.IRON_BLOCK).withOverride(ResourceType.INGOT, Items.IRON_INGOT).withOverride(ResourceType.NUGGET, Items.IRON_NUGGET);
            event.get("gold").setColor(0xfad64a).withOverride(ResourceType.ORE, Blocks.GOLD_ORE).withOverride(ResourceType.METAL_BLOCK, Blocks.GOLD_BLOCK).withOverride(ResourceType.INGOT, Items.GOLD_INGOT).withOverride(ResourceType.NUGGET, Items.GOLD_NUGGET);
            event.get("coal").setColor(0x363636).withOverride(ResourceType.ORE, Blocks.COAL_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.COAL_BLOCK).withOverride(ResourceType.GEM, Items.COAL);
            event.get("lapis_lazuli").setColor(0x345ec3).withOverride(ResourceType.ORE, Blocks.LAPIS_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.LAPIS_BLOCK).withOverride(ResourceType.GEM, Items.LAPIS_LAZULI);
            event.get("diamond").setColor(0x4aedd9).withOverride(ResourceType.ORE, Blocks.DIAMOND_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.DIAMOND_BLOCK).withOverride(ResourceType.GEM, Items.DIAMOND);
            event.get("redstone").setColor(0xaa0f01).withOverride(ResourceType.ORE, Blocks.REDSTONE_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.REDSTONE_BLOCK).withOverride(ResourceType.DUST, Items.REDSTONE);
            event.get("emerald").setColor(0x17dd62).withOverride(ResourceType.ORE, Blocks.EMERALD_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.EMERALD_BLOCK).withOverride(ResourceType.GEM, Items.EMERALD);
            event.get("nether_quartz").setColor(0xddd4c6).withOverride(ResourceType.NETHER_ORE, Blocks.NETHER_QUARTZ_ORE).withOverride(ResourceType.GEM_BLOCK, Blocks.QUARTZ_BLOCK).withOverride(ResourceType.GEM, Items.QUARTZ);
            event.get("glowstone").setColor(0xffbc5e).withOverride(ResourceType.GEM_BLOCK, Blocks.GLOWSTONE).withOverride(ResourceType.DUST, Items.GLOWSTONE_DUST);
        }).subscribe();
    }

    @Override
    public void onInit() {
        ResourceRegistry.onInit();
        super.onInit();
    }

    @Override
    protected void initModules() {
        addModule(Module.builder("core").force()
                .feature(Feature.builder("core").force()
                        .content(ContainerType.class, (ContainerType) IForgeContainerType.create(BasicTileContainer::new).setRegistryName(new ResourceLocation(Titanium.MODID, "tile_container")))));
        addModule(Module.builder("test_module")
                .disableByDefault()
                .description("Test module for titanium features")
                .feature(Feature.builder("blocks")
                        .description("Adds test titanium blocks")
                        .content(Block.class, TestBlock.TEST = new TestBlock())
                        .content(Block.class, TwentyFourTestBlock.TEST = new TwentyFourTestBlock())
                        .content(Block.class, AssetTestBlock.TEST = new AssetTestBlock())
                        .content(Block.class, MachineTestBlock.TEST = new MachineTestBlock())
                        .content(Block.class, TestMultiblockControllerBlock.TEST = new TestMultiblockControllerBlock())
                        .content(Block.class, TestMultiblockFillerBlock.TEST = new TestMultiblockFillerBlock())
                )
                .feature(Feature.builder("events")
                        .description("Adds test titanium events")
                        .event(EventManager.forge(EntityItemPickupEvent.class).filter(ev -> ev.getItem().getItem().getItem() == Items.STICK).process(ev -> ev.getItem().lifespan = 0).cancel())
                )
                .feature(Feature.builder("recipe")
                        .description("Testing of recipe stuff")
                        .content(IRecipeSerializer.class, (IRecipeSerializer) TestSerializableRecipe.SERIALIZER)
                        .event(EventManager.mod(FMLCommonSetupEvent.class).process(event -> Registry.register(Registry.RECIPE_TYPE, TestSerializableRecipe.SERIALIZER.getRegistryName(), TestSerializableRecipe.SERIALIZER.getRecipeType())))
                        .event(EventManager.forge(PlayerInteractEvent.LeftClickBlock.class)
                                .filter(leftClickBlock -> !leftClickBlock.getWorld().isRemote && leftClickBlock.getPlayer() != null)
                                .process(leftClickBlock -> {
                                    Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> recipes = ObfuscationReflectionHelper.getPrivateValue(RecipeManager.class, leftClickBlock.getWorld().getRecipeManager(), "field_199522_d");
                                    recipes.get(TestSerializableRecipe.SERIALIZER.getRecipeType()).values().stream()
                                            .map(iRecipe -> (TestSerializableRecipe) iRecipe)
                                            .filter(testSerializableRecipe -> testSerializableRecipe.isValid(leftClickBlock.getPlayer().getHeldItem(leftClickBlock.getHand()), leftClickBlock.getWorld().getBlockState(leftClickBlock.getPos()).getBlock()))
                                            .findFirst().ifPresent(testSerializableRecipe -> {
                                        leftClickBlock.getPlayer().getHeldItem(leftClickBlock.getHand()).shrink(1);
                                        ItemHandlerHelper.giveItemToPlayer(leftClickBlock.getPlayer(), testSerializableRecipe.getRecipeOutput().copy());
                                        leftClickBlock.setCanceled(true);
                                    });
                                }))
                )
        );
        addModule(Module.builder("creative")
                .disableByDefault()
                .description("Creative features")
                .feature(Feature.builder("blocks")
                        .description("Adds creative machine features")
                        .content(Block.class, CreativeFEGeneratorBlock.INSTANCE)));
        ResourceRegistry.initModules(this);
    }

    @Override
    public void onPostInit() {
        super.onPostInit();
        ResourceRegistry.onPostInit();
    }

    @Override
    public void addDataProvider(GatherDataEvent event) {
        event.getGenerator().addProvider(new BlockItemModelGeneratorProvider(event.getGenerator(), MODID));
        event.getGenerator().addProvider(new DefaultLootTableProvider(event.getGenerator(), MODID));
        event.getGenerator().addProvider(new JsonRecipeSerializerProvider(event.getGenerator(), MODID));
        event.getGenerator().addProvider(new ResourceRegistryProvider(event.getGenerator()));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.DEDICATED_SERVER)));
        LocatorTypes.register();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        EventManager.forge(DrawHighlightEvent.HighlightBlock.class).process(TitaniumClient::blockOverlayEvent).subscribe();
        TitaniumClient.registerModelLoader();
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.CLIENT)));
        ScreenManager.registerFactory(BasicTileContainer.TYPE, BasicTileContainerScreen::new);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().getServer().execute(() -> {
            RewardWorldStorage storage = RewardWorldStorage.get(event.getPlayer().getServer().getWorld(DimensionType.OVERWORLD));
            if (!storage.getConfiguredPlayers().contains(event.getPlayer().getUniqueID())) {
                for (ResourceLocation collectRewardsResourceLocation : RewardManager.get().collectRewardsResourceLocations(event.getPlayer().getUniqueID())) {
                    Reward reward = RewardManager.get().getReward(collectRewardsResourceLocation);
                    storage.add(event.getPlayer().getUniqueID(), reward.getResourceLocation(), reward.getOptions()[0]);
                }
                storage.getConfiguredPlayers().add(event.getPlayer().getUniqueID());
                storage.markDirty();
            }
            CompoundNBT nbt = storage.serializeSimple();
            event.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> Titanium.NETWORK.get().sendTo(new RewardSyncMessage(nbt), serverPlayerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT));
        });
    }

    private void onServerStart(FMLServerStartingEvent event) {
        RewardCommand.register(event.getCommandDispatcher());
        RewardGrantCommand.register(event.getCommandDispatcher());
    }
}