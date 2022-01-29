package net.pixlies.core.listeners.cosmetics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        event.setQuitMessage("§8[§c-§8] §7" + event.getPlayer().getName());
    }

}
