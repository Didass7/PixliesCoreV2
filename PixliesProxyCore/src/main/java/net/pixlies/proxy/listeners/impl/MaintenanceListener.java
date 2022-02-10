package net.pixlies.proxy.listeners.impl;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.pixlies.proxy.Proxy;
import net.pixlies.proxy.config.Config;
import net.pixlies.proxy.handlers.impl.MaintenanceHandler;
import net.pixlies.proxy.utils.CC;

public class MaintenanceListener implements Listener {

    private static final Proxy instance = Proxy.getInstance();

    private final MaintenanceHandler handler = instance.getHandlerManager().getHandler(MaintenanceHandler.class);
    private final Config config = instance.getConfig();

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        if (!handler.isEnabled()) return;
        ProxiedPlayer player = event.getPlayer();
        if (handler.isWhitelisted(player.getUniqueId())) return;
        player.disconnect(CC.asComponent(config.getConfig().getString("message", "&cMaintenance\n&cThe server did not specify a reason.")));
    }

}
