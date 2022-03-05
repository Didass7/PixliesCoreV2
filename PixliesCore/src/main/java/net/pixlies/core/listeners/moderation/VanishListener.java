package net.pixlies.core.listeners.moderation;

import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.VanishHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;

public class VanishListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final VanishHandler handler = Main.getInstance().getHandlerManager().getHandler(VanishHandler.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        // STAFF
        if (user.getPersonalization().isJoinVanish() && player.hasPermission("pixlies.moderation.vanish")) {
            handler.vanish(player);
        }

        // NOT STAFF
        if (!player.hasPermission("pixlies.moderation.vanish.exempt")) return;
        handler.getVanishedPlayers().forEach(target ->
                player.hidePlayer(instance, target));

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isVanished()) return;
        handler.unvanish(player);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isVanished()) return;
        instance.getServer().getOnlinePlayers().forEach(target -> {
            if (target.hasPermission("pixlies.moderation.vanish.exempt")) return;
            target.hidePlayer(instance, player);
        });
    }

    @EventHandler
    public void onCropTrample(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isVanished()) return;
        if (event.getAction() != Action.PHYSICAL) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isVanished()) return;
        event.setCancelled(true);
    }

}
