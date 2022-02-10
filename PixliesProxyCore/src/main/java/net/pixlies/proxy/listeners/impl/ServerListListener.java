package net.pixlies.proxy.listeners.impl;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;
import net.pixlies.proxy.utils.CC;

import java.util.UUID;

public class ServerListListener implements Listener {

    private static final Proxy instance = Proxy.getInstance();

    private final MaintenanceHandler handler = instance.getHandlerManager().getHandler(MaintenanceHandler.class);
    private final Config config = instance.getConfig();
    private final Config serverList = instance.getServerListConfig();

    @EventHandler
    public void onPing(ProxyPingEvent event) {

        // TODO: Move serverlist things to a handler instead of doing it all in a listener
        if (!serverList.getConfig().getBoolean("enabled", false)) return;

        // PING
        ServerPing ping = event.getResponse();
        ping.setDescriptionComponent(CC.asComponent(
                config.getConfig().getString("list.description", "")
                .replace("\\n", "\n")));

        // PROTOCOL
        ServerPing.Protocol protocol = ping.getVersion();
        if (handler.isEnabled()) {
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
