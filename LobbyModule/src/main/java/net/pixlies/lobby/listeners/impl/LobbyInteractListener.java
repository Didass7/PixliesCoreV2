package net.pixlies.lobby.listeners.impl;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import io.papermc.paper.event.entity.EntityDamageItemEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.entity.user.timers.impl.RightClickDelayTimer;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.entity.LobbySpawn;
import net.pixlies.lobby.managers.GrapplingHookManager;
import net.pixlies.lobby.managers.JumpPadManager;
import net.pixlies.lobby.managers.LobbyManager;
import net.pixlies.lobby.utils.JoinItems;
import net.pixlies.lobby.utils.LobbyItem;
import net.pixlies.lobby.utils.LobbyUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class LobbyInteractListener implements Listener {

    private static final Lobby instance = Lobby.getInstance();
    private final LobbyManager lobbyManager = instance.getLobbyManager();
    private final JumpPadManager jumpPadManager = instance.getJumpPadManager();
    private final GrapplingHookManager grappleManager = instance.getGrapplingHookManager();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;

        // PHYSICAL ONLY
        if (event.getAction() == Action.PHYSICAL) {
            final Block block = event.getClickedBlock();
            if (block == null) return;

            // if player is standing on pressure plate
            // multiply everything
            if (block.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                // jump pad

                if (jumpPadManager.isDelayed(player.getUniqueId())) {
                    return;
                }

                Vector velocity = player.getLocation().getDirection();

                player.setVelocity(velocity.multiply(5).setY(1.5));
                player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, SoundCategory.MASTER, Float.MAX_VALUE, 1);
                jumpPadManager.delay(player.getUniqueId());

                return;

            }

        }

        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.FISHING_ROD) {
            return;
        }

        event.setCancelled(true);
        User user = User.get(player.getUniqueId());
        if (item == null) return;
        if (item.getType() == Material.AIR) return;

        if (user.isInStaffMode() && player.hasPermission("pixlies.moderation.staffmode")) {
            return;
        }

        for (LobbyItem lobbyItem : JoinItems.getLobbyItems()) {
            ItemStack itemStack = lobbyItem.getItemStack();
            if (itemStack.getType() == item.getType()) {
                if (!RightClickDelayTimer.isExpired(user)) return;
                lobbyItem.onClick(event);
                new RightClickDelayTimer(player.getUniqueId(), System.currentTimeMillis()).registerTimer();
                return;
            }
        }
    }

    @EventHandler
    public void onGrapple(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        switch (event.getState()) {
            case FISHING -> {
                if (grappleManager.isDelayed(player.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }

                hook.setVelocity(hook.getVelocity().multiply(2));
                hook.addPassenger(player);
                grappleManager.delay(player.getUniqueId());
            }
            case BITE -> event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrame(PlayerItemFrameChangeEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onOffhand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHurtSad(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        }
        if (event.getDamager() instanceof Player damager) {
            if (lobbyManager.isInBuildMode(damager.getUniqueId())) return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onHurtItem(EntityDamageItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDie(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
        LobbyUtils.resetPlayer(player);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());

        if (user.isInStaffMode() && player.hasPermission("pixlies.moderation.staffmode")) {
            return;
        }

        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        if (!(player.getLocation().getY() < 0)) return;

        player.teleport(LobbySpawn.getSpawn().getAsBukkitLocation());

        LobbyUtils.resetPlayer(player);
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (lobbyManager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }

}
