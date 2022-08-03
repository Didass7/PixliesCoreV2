package net.pixlies.core.listeners.player;

import net.pixlies.core.entity.Warp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(Warp.getSpawn().getAsBukkitLocation());
    }

}
