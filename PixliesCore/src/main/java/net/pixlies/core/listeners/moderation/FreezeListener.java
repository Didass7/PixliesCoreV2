package net.pixlies.core.listeners.moderation;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.FreezeHandler;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

/**
 * Class to listen to frozen players
 * @author Dynmie
 */
public class FreezeListener implements Listener {

    private final FreezeHandler handler = Main.getInstance().getHandlerManager().getHandler(FreezeHandler.class);

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (!handler.isFrozen(player.getUniqueId()))
            return;

        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameBreak(PlayerItemFrameChangeEvent event) {
        Player player = event.getPlayer();
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!handler.isFrozen(player.getUniqueId())) return;
        if (player.hasPermission("pixlies.moderation.freeze.exempt")) {
            handler.unfreeze(player);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        handler.unfreeze(event.getPlayer());
    }

}
