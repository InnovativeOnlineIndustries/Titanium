/*
 * This file is part of Titanium
 * Copyright (C) 2021, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.command;

import com.hrznstudio.titanium.reward.Reward;
import com.hrznstudio.titanium.reward.RewardManager;
import com.hrznstudio.titanium.reward.storage.RewardWorldStorage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class RewardGrantCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("titanium-rewards-grant")
                .then(Commands.argument("reward", new ResourceLocationArgument()).suggests((context, builder) -> {
                    return SharedSuggestionProvider.suggest(RewardManager.get().getGiver(context.getSource().getPlayerOrException().getUUID(), context.getSource().getTextName()).getRewards().stream().map(reward -> reward.getResourceLocation().toString()), builder);
                })
                        .executes(context -> {
                            execute(context);
                            return 1;
                        })));
    }

    private static void execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ResourceLocation resourceLocation = context.getArgument("reward", ResourceLocation.class);
        for (Reward reward : RewardManager.get().getGiver(context.getSource().getPlayerOrException().getUUID(), context.getSource().getTextName()).getRewards()) {
            if (reward.getResourceLocation().equals(resourceLocation)) {
                RewardWorldStorage rewardWorldStorage = RewardWorldStorage.get(context.getSource().getLevel());
                rewardWorldStorage.addFree(resourceLocation);
                rewardWorldStorage.setDirty();
                context.getSource().sendSuccess(new TranslatableComponent("titanium.rewards.granted_success"), true);
                return;
            }
        }
    }
}
