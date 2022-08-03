package net.pixlies.core.listeners.player;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.TeleportHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final TeleportHandler tpHandler = instance.getHandlerManager().getHandler(TeleportHandler.class);

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getFrom();
        User user = User.get(player.getUniqueId());
        if (user.isPassive()) return;
        tpHandler.setBackLocation(player.getUniqueId(), loc);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        tpHandler.removeBackLocation(player.getUniqueId());
        tpHandler.removeTpAskPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (user.isPassive()) return;
        tpHandler.setBackLocation(player.getUniqueId(), player.getLocation());
    }

}
