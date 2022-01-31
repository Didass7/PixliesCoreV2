package net.pixlies.proxy.listeners.impl;

import co.aikar.commands.annotation.Dependency;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;
import net.pixlies.proxy.utils.CC;

public class MaintenanceListener implements Listener {

    @Dependency("maintenanceHandler") MaintenanceHandler handler;
    @Dependency("maintenanceConfig") Config config;

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        if (!handler.isEnabled()) return;
        ProxiedPlayer player = event.getPlayer();
        if (handler.isWhitelisted(player.getUniqueId())) return;
        player.disconnect(CC.asComponent(config.getConfig().getString("message", "&cMaintenance\n&cThe server did not specify a reason.")));
    }

}
