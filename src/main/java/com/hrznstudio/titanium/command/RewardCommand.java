/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.command;

import com.hrznstudio.titanium.Titanium;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.RewardSyncMessage;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;

public class RewardCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("titanium-rewards")
                .then(Commands.argument("action", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            return ISuggestionProvider.suggest(new String[]{"enable", "disable"}, builder);
                        }).then(Commands.argument("reward", new ResourceLocationArgument())
                                .suggests((context, builder) -> {
                                    return ISuggestionProvider.suggest(getAvailableResourceLocations(context).stream().map(ResourceLocation::toString), builder);
                                }).then(Commands.argument("option", StringArgumentType.word()).suggests((context, builder) -> {
                                    return ISuggestionProvider.suggest(RewardManager.get().getReward(context.getArgument("reward", ResourceLocation.class)).getOptions(), builder);
                                })
                                        .executes(context -> {
                                            execute(context);
                                            return 1;
                                        })))));

    }

    private static void execute(CommandContext<CommandSource> context) {
        boolean changed = false;
        if (context.getArgument("action", String.class).equalsIgnoreCase("enable")) {
            changed = addReward(context);
        } else {
            changed = removeReward(context);
        }
        if (changed) {
            context.getSource().getServer().execute(() -> {
                CompoundNBT nbt = RewardWorldStorage.get(context.getSource().getServer().getWorld(DimensionType.OVERWORLD)).serializeSimple();
                context.getSource().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> Titanium.NETWORK.get().sendTo(new RewardSyncMessage(nbt), serverPlayerEntity.connection.netManager, NetworkDirection.PLAY_TO_CLIENT));
            });
        }
    }

    private static boolean removeReward(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(source.getWorld());
        try {
            rewardWorldStorage.remove(source.asPlayer().getUniqueID(), context.getArgument("reward", ResourceLocation.class));
            rewardWorldStorage.markDirty();
            context.getSource().sendFeedback(new TranslationTextComponent("titanium.rewards.remove_success"), true);
            return true;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean addReward(CommandContext<CommandSource> context) {
        try {
            CommandSource source = context.getSource();
            ResourceLocation resourceLocation = context.getArgument("reward", ResourceLocation.class);
            if (RewardManager.get().getReward(resourceLocation) == null || !RewardManager.get().getReward(resourceLocation).isPlayerValid(source.asPlayer().getUniqueID()))
                return false;
            RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(source.getWorld());
            rewardWorldStorage.add(source.asPlayer().getUniqueID(), context.getArgument("reward", ResourceLocation.class), context.getArgument("option", String.class));
            rewardWorldStorage.markDirty();
            context.getSource().sendFeedback(new TranslationTextComponent("titanium.rewards.enabled_success"), true);
            return true;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static List<ResourceLocation> getAvailableResourceLocations(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(source.getWorld());
        List<ResourceLocation> resourceLocations = new ArrayList<>(rewardWorldStorage.getFreeRewards());
        try {
            resourceLocations.addAll(RewardManager.get().collectRewardsResourceLocations(context.getSource().asPlayer().getUniqueID()));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return resourceLocations;
    }
}
