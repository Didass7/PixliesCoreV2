package net.pixlies.proxy.listeners.impl;

import co.aikar.commands.annotation.Dependency;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.utils.CC;

import java.util.UUID;

public class ServerListListener implements Listener {

    @Dependency Config config;
    @Dependency("serverListConfig") Config serverList;

    @EventHandler
    public void onPing(ProxyPingEvent event) {

        if (!serverList.getConfig().getBoolean("disabled", false)) return;

        // PING
        ServerPing ping = event.getResponse();
        ping.setDescriptionComponent(CC.asComponent(
                config.getConfig().getString("list.description", "")
                .replace("\\n", "\n")));

        // PROTOCOL
        // TODO: MAINTENANCE MODE
        ServerPing.Protocol protocol = ping.getVersion();
        if (true /* MAINTENANCE MODE */) {
            protocol.setProtocol(serverList.getConfig().getInt("maintenance.protocol", 2));
            protocol.setName(serverList.getConfig().getString("maintenance.message", ""));
        } else {
            protocol.setName(serverList.getConfig().getString("version.message", ""));
        }

        // PLAYERS
        ServerPing.Players players = ping.getPlayers();
        if (serverList.getConfig().getString("playerlist") != null && serverList.getConfig().getString("playerlist").isEmpty()) {
            players.setSample(new ServerPing.PlayerInfo[] {
                    new ServerPing.PlayerInfo(CC.format(serverList.getConfig().getString("playerlist.message")), UUID.randomUUID())
            });
        }

        event.setResponse(ping);

    }

}
