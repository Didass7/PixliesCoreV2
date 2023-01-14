package net.pixlies.core.listeners.player;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TabListHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TabListListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final TabListHandler handler = instance.getHandlerManager().getHandler(TabListHandler.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!handler.isLoaded()) return;
        handler.formatTabListFor(event.getPlayer());
        handler.sortTabList();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!handler.isLoaded()) return;
        handler.updateDisplayNames();
    }

}
