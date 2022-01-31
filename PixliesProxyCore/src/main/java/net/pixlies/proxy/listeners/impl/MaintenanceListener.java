package net.pixlies.proxy.listeners.impl;

import co.aikar.commands.annotation.Dependency;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;
import net.pixlies.proxy.utils.CC;

import java.util.UUID;

public class MaintenanceListener implements Listener {

    @Dependency("maintenanceHandler") MaintenanceHandler handler;
    @Dependency("maintenanceConfig") Config config;

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        if (!handler.isEnabled()) return;
        UUID uuid = event.getConnection().getUniqueId();
        if (!handler.isPlayerWhitelisted(uuid))
        event.setCancelled(true);
        event.setCancelReason(CC.asComponent(config.getConfig().getString("message", "&cMaintenance\n&cThe server did not specify a reason.")));
    }

}
