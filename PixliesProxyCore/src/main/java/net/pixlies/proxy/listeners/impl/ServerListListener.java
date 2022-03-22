package net.pixlies.proxy.listeners.impl;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;
import net.pixlies.proxy.handlers.impl.ServerListHandler;
import net.pixlies.proxy.utils.CC;

import java.util.UUID;

public class ServerListListener implements Listener {

    private static final Proxy instance = Proxy.getInstance();

    private final MaintenanceHandler maintenanceHandler = instance.getHandlerManager().getHandler(MaintenanceHandler.class);
    private final ServerListHandler serverListHandler = instance.getHandlerManager().getHandler(ServerListHandler.class);

    @EventHandler
    public void onPing(ProxyPingEvent event) {

        if (!serverListHandler.isEnabled()) return;

        // PING
        ServerPing ping = event.getResponse();
        ping.setDescriptionComponent(CC.asComponent(serverListHandler.getDescription()));

        // PROTOCOL
        ServerPing.Protocol protocol = ping.getVersion();
        if (maintenanceHandler.isEnabled()) {
            protocol.setProtocol(serverListHandler.getMaintenanceProtocol());
            protocol.setName(serverListHandler.getMaintenanceMessage());
        } else {
            protocol.setName(serverListHandler.getVersionMessage());
        }

        // PLAYERS
        ServerPing.Players players = ping.getPlayers();
        if (serverListHandler.hasPlayerListMessage()) {
            players.setSample(new ServerPing.PlayerInfo[] {
                    new ServerPing.PlayerInfo(CC.format(serverListHandler.getPlayerListMessage()), UUID.randomUUID())
            });
        }

        event.setResponse(ping);

    }

}
