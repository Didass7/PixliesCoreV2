package net.pixlies.core.listeners.moderation;

import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.events.impl.moderation.VanishStatusChangeEvent;
import net.pixlies.core.handlers.impl.VanishHandler;
import net.pixlies.core.handlers.impl.staffmode.StaffModeHandler;
import net.pixlies.core.localization.Lang;
import net.pixlies.core.utils.CC;
import net.pixlies.core.utils.ItemBuilder;
import net.pixlies.core.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class StaffModeListener implements Listener {

    private static final Main instance = Main.getInstance();
    private final StaffModeHandler staffModeHandler = instance.getHandlerManager().getHandler(StaffModeHandler.class);
    private final VanishHandler vanishHandler = instance.getHandlerManager().getHandler(VanishHandler.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (instance.isLobbyServer()) return;
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        staffModeHandler.enableWithoutUpdate(player, User.get(player.getUniqueId()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (instance.isLobbyServer()) return;
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        staffModeHandler.disableWithoutUpdate(player, user);
    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player damager) {
            User user = User.get(damager.getUniqueId());
            if (user.getSettings().isStaffModeEnabled())
                event.setCancelled(true);
        } else if (event.getEntity() instanceof Player victim) {
            User user = User.get(victim.getUniqueId());
            if (user.getSettings().isStaffModeEnabled())
                event.setCancelled(true);
        }

    }

    @EventHandler
    public void onCropTrample(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (user.getSettings().isStaffModeEnabled()) return;
        if (event.getAction() == Action.PHYSICAL) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;

        if (event.getAction() == Action.PHYSICAL) return;

        switch (player.getInventory().getItemInMainHand().getType()) {

            case CLOCK -> {
                Player target = PlayerUtils.getRandomPlayer(player);
                if (target == null) {
                    Lang.TPACCEPT_PLAYER_NOT_ONLINE.send(player);
                    break;
                }
                player.teleport(target.getLocation());
                Lang.STAFF_RANDOM_TELEPORT.send(player, "%TARGET%;" + target.getName());
            }

            case LIME_DYE -> {
                vanishHandler.unvanish(player);
                player.getInventory().setItem(8, StaffModeHandler.getVanishItem(false));
            }

            case GRAY_DYE -> {
                vanishHandler.vanish(player);
                player.getInventory().setItem(8, StaffModeHandler.getVanishItem(true));
            }

        }

        event.setCancelled(true);

    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {

        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.getSettings().isStaffModeEnabled()) return;
        event.setCancelled(true);

        if (!(event.getRightClicked() instanceof Player target)) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.BOOK || player.getInventory().getItemInMainHand().getType() != Material.PACKED_ICE) return;

        switch (player.getInventory().getItemInMainHand().getType()) {
            case BOOK -> player.performCommand("invsee " + target.getName());
            case PACKED_ICE -> player.performCommand("freeze " + target.getName());
        }

    }

    @EventHandler
    public void onVanish(VanishStatusChangeEvent event) {

        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        VanishStatusChangeEvent.VanishState state = event.getState();

        if (!user.getSettings().isStaffModeEnabled()) return;

        switch (state) {
            case VANISH -> player.getInventory().setItem(8, StaffModeHandler.getVanishItem(true));
            case UNVANISH -> player.getInventory().setItem(8, StaffModeHandler.getVanishItem(false));
        }

    }

}
