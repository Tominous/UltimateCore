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
package bammerbom.ultimatecore.sponge.utils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VariableUtil {
    public static Text replaceVariables(Text text, @Nullable CommandSource player) {
        //Player-specific variables
        if (player != null) {
            text = TextUtil.replace(text, "%player%", getNameSource(player));
            text = TextUtil.replace(text, "%name%", Text.of(player.getName()));
            text = TextUtil.replace(text, "%displayname%", getNameSource(player));
            text = TextUtil.replace(text, "%prefix%", Text.of(player.getOption("prefix").orElse("")));
            text = TextUtil.replace(text, "%suffix%", Text.of(player.getOption("suffix").orElse("")));
            if (player instanceof Player) {
                Player p = (Player) player;
                text = TextUtil.replace(text, "%world%", Text.of(p.getWorld().getName()));
                text = TextUtil.replace(text, "%worldalias%", Text.of(p.getWorld().getName().toCharArray()[0] + ""));
                text = TextUtil.replace(text, "%ip%", Text.of(p.getConnection().getAddress().getAddress().toString().split("/")[1].split(":")[0]));
                if (Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
                    EconomyService es = Sponge.getServiceManager().provide(EconomyService.class).get();
                    if (es.getOrCreateAccount(p.getUniqueId()).isPresent()) {
                        BigDecimal balance = es.getOrCreateAccount(p.getUniqueId()).get().getBalance(es.getDefaultCurrency());
                        text = TextUtil.replace(text, "%money%", Text.of(balance.toString()));
                    }
                }
            }
        }

        //Not player-specific variables
        text = TextUtil.replace(text, "%version%", Text.of(Sponge.getPlatform().getMinecraftVersion().getName()));
        List<String> names = new ArrayList<>();
        Sponge.getServer().getOnlinePlayers().forEach(p -> names.add(p.getName()));
        text = TextUtil.replace(text, "%players%", !names.isEmpty() ? Text.of(StringUtil.join(", ", names)) : Messages.getFormatted("core.none"));
        text = TextUtil.replace(text, "%maxplayers%", Text.of(Sponge.getServer().getMaxPlayers()));
        text = TextUtil.replace(text, "%onlineplayers%", Text.of(Sponge.getServer().getOnlinePlayers().size()));

        return text;
    }

    public static Text replaceVariablesUser(Text text, @Nullable User player) {
        text = replaceVariables(text, null);
        if (player != null) {
            text = TextUtil.replace(text, "%player%", getNameUser(player));
            text = TextUtil.replace(text, "%name%", Text.of(player.getName()));
            text = TextUtil.replace(text, "%displayname%", getNameUser(player));
            text = TextUtil.replace(text, "%prefix%", Text.of(player.getOption("prefix").orElse("")));
            text = TextUtil.replace(text, "%suffix%", Text.of(player.getOption("suffix").orElse("")));
            if (Sponge.getServiceManager().provide(EconomyService.class).isPresent()) {
                EconomyService es = Sponge.getServiceManager().provide(EconomyService.class).get();
                if (es.getOrCreateAccount(player.getUniqueId()).isPresent()) {
                    BigDecimal balance = es.getOrCreateAccount(player.getUniqueId()).get().getBalance(es.getDefaultCurrency());
                    text = TextUtil.replace(text, "%money%", Text.of(balance.toString()));
                }
            }
        }
        return text;
    }

    public static Text getNameSource(CommandSource player) {
        //TODO nickname
        if (player instanceof Player) {
            return Text.builder(player.getName()).onHover(TextActions.showText(Messages.getFormatted("core.variable.player.hover", "%name%", player.getName(), "%rawname%", player.getName(), "%uuid%", ((Player) player).getUniqueId(), "%language%", player.getLocale().getDisplayName(Locale.ENGLISH)))).onClick(TextActions.suggestCommand(Messages.getFormatted("core.variable.player.click", "%player%", player.getName()).toPlain())).build();
        } else {
            return Text.builder(player.getName()).onHover(TextActions.showText(Messages.getFormatted("core.variable.player.hover", "%name%", player.getName(), "%rawname%", player.getName(), "%uuid%", player.getIdentifier(), "%language%", player.getLocale().getDisplayName(Locale.ENGLISH)))).onClick(TextActions.suggestCommand(Messages.getFormatted("core.variable.player.click", "%player%", player.getName()).toPlain())).build();
        }
    }

    public static Text getNameEntity(Entity en) {
        if (en instanceof CommandSource) {
            return getNameSource((CommandSource) en);
        } else {
            return Text.of(en.getTranslation().get(Locale.ENGLISH));
        }
    }

    public static Text getNamesEntity(List<Entity> ens) {
        List<Text> texts = new ArrayList<>();
        for (Entity en : ens) {
            texts.add(getNameEntity(en));
        }
        return Text.joinWith(Text.of(", "), texts);
    }

    public static Text getNameUser(User player) {
        if (player instanceof Player) {
            return getNameSource((CommandSource) player);
        }
        //TODO language?
        return Text.builder(player.getName()).onHover(TextActions.showText(Messages.getFormatted("core.variable.player.hover", "%name%", player.getName(), "%rawname%", player.getName(), "%uuid%", player.getUniqueId(), "%language%", "?"))).onClick(TextActions.suggestCommand(Messages.getFormatted("core.variable.player.click", "%player%", player.getName()).toPlain())).build();
    }
}
