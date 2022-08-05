package net.pixlies.proxy.listeners.impl.profile;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.pixlies.proxy.PixliesProxy;

public class PixliesProfileListener implements Listener {

    private static final PixliesProxy instance = PixliesProxy.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        instance.getMongoManager().getProfileCache().remove(player.getUniqueId());
    }

}
