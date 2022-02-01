package net.pixlies.core.listeners.moderation;

import net.pixlies.core.Main;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final VanishHandler handler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!handler.isVanished(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!handler.isVanished(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!handler.isVanished(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!handler.isVanished(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!handler.isVanished(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("pixlies.moderation.vanish.exempt")) return;
        handler.getVanishedPlayers().forEach(target -> player.hidePlayer(instance, target));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!handler.isVanished(player.getUniqueId())) return;
        handler.unvanish(player);
    }

}
