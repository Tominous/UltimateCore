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
package bammerbom.ultimatecore.sponge.modules.ban;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.ModuleConfig;
import bammerbom.ultimatecore.sponge.api.config.defaultconfigs.module.RawModuleConfig;
import bammerbom.ultimatecore.sponge.api.module.HighModule;
import bammerbom.ultimatecore.sponge.api.module.annotations.ModuleInfo;
import bammerbom.ultimatecore.sponge.modules.ban.commands.BanCommand;
import bammerbom.ultimatecore.sponge.modules.ban.commands.IpCommand;
import bammerbom.ultimatecore.sponge.modules.ban.commands.UnbanCommand;
import bammerbom.ultimatecore.sponge.modules.ban.listeners.BanListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.state.GameInitializationEvent;

import java.util.Optional;

@ModuleInfo(name = "ban", description = "Allows you to ban and unban players and ips")
public class BanModule implements HighModule {
    ModuleConfig config;

    @Override
    public Optional<? extends ModuleConfig> getConfig() {
        return Optional.of(this.config);
    }

    @Override
    public void onInit(GameInitializationEvent event) {
        this.config = new RawModuleConfig("ban");

        UltimateCore.get().getCommandService().register(new BanCommand());
        UltimateCore.get().getCommandService().register(new UnbanCommand());
        UltimateCore.get().getCommandService().register(new IpCommand());

        Sponge.getEventManager().registerListeners(UltimateCore.get(), new BanListener());
    }
}
