package net.pixlies.core.listeners.player;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.TabListHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListListener implements Listener {

    private static final Main instance = Main.getInstance();
    private static final TabListHandler handler = instance.getHandlerManager().getHandler(TabListHandler.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!handler.isLoaded()) return;
        handler.formatTabListFor(event.getPlayer());
        handler.updateDisplayNames();
        handler.sortTabList();
    }

}
