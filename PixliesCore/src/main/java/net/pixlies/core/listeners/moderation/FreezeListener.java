package net.pixlies.core.listeners.moderation;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.FreezeHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Class to listen to frozen players
 * @author Dynmie
 */
public class FreezeListener implements Listener {

    private final FreezeHandler handler = Main.getInstance().getHandlerManager().getHandler(FreezeHandler.class);

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (event.hasChangedOrientation()) return;
        Player player = event.getPlayer();

        if (!handler.isFrozen(player.getUniqueId()))
            return;

        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
        }

        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        handler.unfreeze(event.getPlayer());
    }

}
