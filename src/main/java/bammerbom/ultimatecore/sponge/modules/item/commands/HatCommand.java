/*
 * This file is part of UltimateCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) Bammerbom
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bammerbom.ultimatecore.sponge.modules.item.commands;

import bammerbom.ultimatecore.sponge.api.command.HighPermCommand;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandInfo;
import bammerbom.ultimatecore.sponge.api.command.annotations.CommandPermissions;
import bammerbom.ultimatecore.sponge.api.command.argument.Arguments;
import bammerbom.ultimatecore.sponge.api.command.argument.arguments.PlayerArgument;
import bammerbom.ultimatecore.sponge.api.language.utils.Messages;
import bammerbom.ultimatecore.sponge.api.permission.PermissionInfo;
import bammerbom.ultimatecore.sponge.api.permission.PermissionLevel;
import bammerbom.ultimatecore.sponge.modules.item.ItemModule;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.HashMap;
import java.util.Map;

@CommandInfo(module = ItemModule.class, aliases = {"hat"})
@CommandPermissions(level = PermissionLevel.VIP)
public class HatCommand implements HighPermCommand {
    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                Arguments.builder(new PlayerArgument(Text.of("player"))).onlyOne().optional().build()
        };
    }

    @Override
    public Map<String, PermissionInfo> registerPermissionSuffixes() {
        HashMap<String, PermissionInfo> suffixes = new HashMap<>();
        suffixes.put("others", new PermissionInfo(Text.of("Allows you to use the hat command on other players."), PermissionLevel.ADMIN));
        return suffixes;
    }

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        checkIfPlayer(sender);
        Player p = (Player) sender;
        ItemStack hand = p.getItemInHand(HandTypes.MAIN_HAND);
        if (!args.hasAny("player")) {
            ItemStack headOp = p.getHelmet();
            p.setHelmet(hand);
            p.setItemInHand(HandTypes.MAIN_HAND, headOp);
            Messages.send(sender, "item.command.hat.self");
        } else {
            checkPermSuffix(sender, "others");
            Player t = args.<Player>getOne("player").get();
            ItemStack headOp = t.getHelmet();
            t.setHelmet(hand);
            t.getInventory().offer(hand);
            p.setItemInHand(HandTypes.MAIN_HAND, null);
            Messages.send(sender, "item.command.hat.others.self", "%player%", t);
            Messages.send(t, "item.command.hat.others.others", "%player%", t);
        }
        return CommandResult.success();
    }
}
