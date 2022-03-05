package net.pixlies.core.listeners.moderation;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import net.pixlies.core.entity.user.User;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Passive listener to listen
 * @author Dynmie
 */
public class PassiveListener implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDie(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        event.setCancelled(true);
    }

    /**
     * Open chests silently.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isPassive()) return;
        if (!event.getAction().isRightClick()) return;

        Block block = event.getClickedBlock();
        if (block == null)
            return;

        if (!(block instanceof Chest chest))
            return;

        if (player.hasPermission("pixlies.moderation.chest.silent")) {
            event.setCancelled(true);
            player.openInventory(chest.getInventory());
        }
    }

}
