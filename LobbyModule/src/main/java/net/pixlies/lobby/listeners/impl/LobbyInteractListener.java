package net.pixlies.lobby.listeners.impl;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import io.papermc.paper.event.entity.EntityDamageItemEvent;
import net.pixlies.core.entity.user.User;
import net.pixlies.lobby.Lobby;
import net.pixlies.lobby.managers.LobbyManager;
import net.pixlies.lobby.utils.JoinItems;
import net.pixlies.lobby.utils.LobbyItem;
import net.pixlies.lobby.utils.LobbyUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyInteractListener implements Listener {

    private static final Lobby instance = Lobby.getInstance();
    private final LobbyManager manager = instance.getLobbyManager();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);

        ItemStack item = event.getItem();
        if (item == null) return;
        if (item.getType() == Material.AIR) return;

        for (LobbyItem lobbyItem : JoinItems.getLobbyItems()) {
            ItemStack itemStack = lobbyItem.getItemStack();
            if (itemStack.getType() == item.getType()) {
                lobbyItem.onClick(event);
                return;
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onOffhand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHurtSad(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof Player damager)) return;
        if (manager.isInBuildMode(player.getUniqueId())) return;
        if (manager.isInBuildMode(damager.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHurtSad(EntityDamageItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (manager.isInBuildMode(player.getUniqueId())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (manager.isInBuildMode(player.getUniqueId())) return;
        if (!(player.getLocation().getY() < 0)) return;

        User user = User.get(player.getUniqueId());
        user.teleportToSpawn();

        LobbyUtils.resetPlayer(player);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
        event.setCancelled(true);
    }

}
