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
import com.hrznstudio.titanium._impl.test.recipe.TestSerializableRecipe;
import com.hrznstudio.titanium.annotation.plugin.FeaturePlugin;
import com.hrznstudio.titanium.capability.CapabilityItemStackHolder;
import com.hrznstudio.titanium.client.screen.container.BasicAddonScreen;
import com.hrznstudio.titanium.command.RewardCommand;
import com.hrznstudio.titanium.command.RewardGrantCommand;
import com.hrznstudio.titanium.container.BasicAddonContainer;
import com.hrznstudio.titanium.datagenerator.loot.TitaniumLootTableProvider;
import com.hrznstudio.titanium.datagenerator.model.BlockItemModelGeneratorProvider;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.material.ResourceRegistry;
import com.hrznstudio.titanium.module.Feature;
import com.hrznstudio.titanium.module.Module;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import com.hrznstudio.titanium.network.locator.LocatorTypes;
import com.hrznstudio.titanium.network.messages.ButtonClickNetworkMessage;
import com.hrznstudio.titanium.plugin.PluginManager;
import com.hrznstudio.titanium.recipe.condition.ContentExistsConditionSerializer;
import com.hrznstudio.titanium.recipe.generator.titanium.JsonRecipeSerializerProvider;
import com.hrznstudio.titanium.recipe.generator.titanium.ResourceRegistryProvider;
import com.hrznstudio.titanium.recipe.shapelessenchant.ShapelessEnchantSerializer;
import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.RewardSyncMessage;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.hrznstudio.titanium.util.SidedHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.util.NonNullLazy;
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
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Mod(Titanium.MODID)
public class Titanium extends ModuleController {

    public static final String MODID = "titanium";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static NetworkHandler NETWORK = new NetworkHandler(MODID);
    public static final PluginManager RESOURCES = new PluginManager(MODID, FeaturePlugin.FeaturePluginType.FEATURE, featurePlugin -> featurePlugin.value().equalsIgnoreCase(ResourceRegistry.PLUGIN_NAME), false);

    public Titanium() {
        NETWORK.registerMessage(ButtonClickNetworkMessage.class);
        NETWORK.registerMessage(RewardSyncMessage.class);
        SidedHandler.runOn(Dist.CLIENT, () -> () -> EventManager.mod(FMLClientSetupEvent.class).process(this::clientSetup).subscribe());
        EventManager.mod(FMLCommonSetupEvent.class).process(this::commonSetup).subscribe();
        EventManager.forge(PlayerEvent.PlayerLoggedInEvent.class).process(this::onPlayerLoggedIn).subscribe();
        EventManager.forge(FMLServerStartingEvent.class).process(this::onServerStart).subscribe();
        CraftingHelper.register(new ContentExistsConditionSerializer());
    }

    @Override
    public void onPreInit() {
        super.onPreInit();
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
                        .content(ContainerType.class, (ContainerType) IForgeContainerType.create(BasicAddonContainer::create).setRegistryName(new ResourceLocation(Titanium.MODID, "addon_container")))
                        .content(IRecipeSerializer.class, (IRecipeSerializer)new ShapelessEnchantSerializer().setRegistryName(new ResourceLocation(Titanium.MODID, "shapeless_enchant")))
                )
        );
        addModule(Module.builder("test_module")
                .disableByDefault()
                .description("Test module for titanium features")
                .feature(Feature.builder("blocks")
                        .description("Adds test titanium blocks")
                        .content(Block.class, TestBlock.TEST = (TestBlock) new TestBlock().setRegistryName("block_test"))
                        .content(Block.class, TwentyFourTestBlock.TEST = (TwentyFourTestBlock) new TwentyFourTestBlock().setRegistryName("block_twenty_four_test"))
                        .content(Block.class, AssetTestBlock.TEST = (AssetTestBlock) new AssetTestBlock().setRegistryName("block_asset_test"))
                        .content(Block.class, MachineTestBlock.TEST = (MachineTestBlock) new MachineTestBlock().setRegistryName("machine_test"))
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
        NonNullLazy<List<Block>> blocksToProcess = NonNullLazy.of(() ->
            ForgeRegistries.BLOCKS.getValues()
                .stream()
                .filter(basicBlock -> Optional.ofNullable(basicBlock.getRegistryName())
                    .map(ResourceLocation::getNamespace)
                    .filter(MODID::equalsIgnoreCase)
                    .isPresent())
                .collect(Collectors.toList())
        );
        event.getGenerator().addProvider(new BlockItemModelGeneratorProvider(event.getGenerator(), MODID, blocksToProcess));
        event.getGenerator().addProvider(new TitaniumLootTableProvider(event.getGenerator(), blocksToProcess));
        event.getGenerator().addProvider(new JsonRecipeSerializerProvider(event.getGenerator(), MODID));
        event.getGenerator().addProvider(new ResourceRegistryProvider(event.getGenerator()));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityItemStackHolder.register();
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.DEDICATED_SERVER)));
        LocatorTypes.register();
    }

    @OnlyIn(Dist.CLIENT)
    private void clientSetup(FMLClientSetupEvent event) {
        EventManager.forge(DrawHighlightEvent.HighlightBlock.class).process(TitaniumClient::blockOverlayEvent).subscribe();
        TitaniumClient.registerModelLoader();
        RewardManager.get().getRewards().values().forEach(rewardGiver -> rewardGiver.getRewards().forEach(reward -> reward.register(Dist.CLIENT)));
        ScreenManager.registerFactory(BasicAddonContainer.TYPE, BasicAddonScreen::new);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getPlayer().getServer().execute(() -> {
            RewardWorldStorage storage = RewardWorldStorage.get(event.getPlayer().getServer().getWorld(World.OVERWORLD));
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
        RewardCommand.register(event.getServer().getCommandManager().getDispatcher());
        RewardGrantCommand.register(event.getServer().getCommandManager().getDispatcher());
    }
}
