package net.pixlies.proxy.listeners.impl;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.Proxy;

import java.util.concurrent.TimeUnit;

public class ServerKickListener implements Listener {

    private static final Plugin plugin = Proxy.getInstance();

    @EventHandler
    public void onKick(final ServerKickEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        if (player.getServer() != null) {
            if (!player.getServer().getInfo().getName().equalsIgnoreCase("Lobby")) {
                event.setCancelled(true);
                plugin.getProxy().getScheduler().schedule(plugin, () -> player.connect(plugin.getProxy().getServerInfo("Lobby")), 1, TimeUnit.MICROSECONDS);
            }
        }
    }

}
