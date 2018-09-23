/*
 * This file is part of Titanium
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot steal this code. This is licensed for sole use by Horizon Studio and its subsidiaries, you MUST be granted specific written permission by Horizon Studio to use this code, thinking you have permission IS NOT PERMISSION!
 */
package com.hrznstudio.titanium;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class CommandNickname extends CommandBase {

    public static List<String> aliases = Lists.newArrayList("nick");

    @Override
    public String getName() {
        return "nickname";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/nickname";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.sendMessage(new TextComponentString("No nickname specified"));
            return;
        }
        String nickname = args[0];
        EntityPlayer player;
        if (args.length > 1) {
            player = getPlayer(server, sender, args[1]);
        } else {
            if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
                player = (EntityPlayer) sender.getCommandSenderEntity();
            } else {
                sender.sendMessage(new TextComponentString("Need to specify a target"));
                return;
            }
        }

        player.setCustomNameTag(nickname);
    }
}
