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
package bammerbom.ultimatecore.sponge.modules.serverlist.listeners;

import bammerbom.ultimatecore.sponge.UltimateCore;
import bammerbom.ultimatecore.sponge.api.module.Modules;
import bammerbom.ultimatecore.sponge.api.user.UltimateUser;
import bammerbom.ultimatecore.sponge.config.ModuleConfig;
import bammerbom.ultimatecore.sponge.config.datafiles.DataFile;
import bammerbom.ultimatecore.sponge.utils.Messages;
import bammerbom.ultimatecore.sponge.utils.VariableUtil;
import com.google.common.reflect.TypeToken;
import net.minecrell.statusprotocol.StatusProtocol;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ServerlistListener {
    static Random random = new Random();

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {

    }

    @Listener
    public void onMotdSend(ClientPingServerEvent event) {
        try {
            String ip = event.getClient().getAddress().getAddress().toString().replace("/", "");
            DataFile file = new DataFile("ipcache");
            ModuleConfig config = Modules.SERVERLIST.get().getConfig().get();
            if (file.get().getChildrenMap().keySet().contains(ip)) {
                //Player
                UserStorageService service = Sponge.getServiceManager().provide(UserStorageService.class).get();
                User p = service.get(UUID.fromString(file.get().getNode(ip, "uuid").getString())).orElse(null);
                if (p == null) return;
                UltimateUser up = UltimateCore.get().getUserService().getUser(p);

                //Motd
                if (config.get().getNode("player", "motd", "enable").getBoolean()) {
                    List<String> motds = config.get().getNode("player", "motd", "motds").getList(TypeToken.of(String.class));
                    Text motd = VariableUtil.replaceVariablesUser(Messages.toText(motds.get(random.nextInt(motds.size()))), p);
                    event.getResponse().setDescription(motd);
                }

                //Version info
                if (config.get().getNode("player", "playercount", "enable").getBoolean()) {
                    List<String> formats = config.get().getNode("player", "playercount", "counter").getList(TypeToken.of(String.class));
                    Text format = VariableUtil.replaceVariablesUser(Messages.toText(formats.get(random.nextInt(formats.size()))), p);
                    StatusProtocol.setVersion(event.getResponse(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(format), 1);
                }

                //Playercounter hover
                if (config.get().getNode("player", "playerhover", "enable").getBoolean()) {
                    List<String> hovers = config.get().getNode("player", "playerhover", "hover").getList(TypeToken.of(String.class));
                    Text hover = VariableUtil.replaceVariablesUser(Messages.toText(hovers.get(random.nextInt(hovers.size()))), p);
                    if (event.getResponse().getPlayers().isPresent()) {
                        ClientPingServerEvent.Response.Players players = event.getResponse().getPlayers().get();
                        players.getProfiles().clear();
                        players.getProfiles().add(GameProfile.of(UUID.randomUUID(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(hover)));
                    }
                }

            } else {
                //Unknown

                //Motd
                if (config.get().getNode("unknown", "motd", "enable").getBoolean()) {
                    List<String> motds = config.get().getNode("unknown", "motd", "motds").getList(TypeToken.of(String.class));
                    Text motd = VariableUtil.replaceVariablesUser(Messages.toText(motds.get(random.nextInt(motds.size()))), null);
                    event.getResponse().setDescription(motd);
                }

                //Version info
                if (config.get().getNode("unknown", "playercount", "enable").getBoolean()) {
                    List<String> formats = config.get().getNode("unknown", "playercount", "counter").getList(TypeToken.of(String.class));
                    Text format = VariableUtil.replaceVariablesUser(Messages.toText(formats.get(random.nextInt(formats.size()))), null);
                    StatusProtocol.setVersion(event.getResponse(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(format), 1);
                }

                //Playercounter hover
                if (config.get().getNode("unknown", "playerhover", "enable").getBoolean()) {
                    List<String> hovers = config.get().getNode("unknown", "playerhover", "hover").getList(TypeToken.of(String.class));
                    Text hover = VariableUtil.replaceVariablesUser(Messages.toText(hovers.get(random.nextInt(hovers.size()))), null);
                    if (event.getResponse().getPlayers().isPresent()) {
                        ClientPingServerEvent.Response.Players players = event.getResponse().getPlayers().get();
                        players.getProfiles().clear();
                        players.getProfiles().add(GameProfile.of(UUID.randomUUID(), TextSerializers.LEGACY_FORMATTING_CODE.serialize(hover)));
                    }
                }
            }
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }
}
