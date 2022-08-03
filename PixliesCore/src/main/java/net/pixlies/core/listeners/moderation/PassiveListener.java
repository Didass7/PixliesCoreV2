package net.pixlies.core.listeners.moderation;

import com.destroystokyo.paper.event.player.PlayerAdvancementCriterionGrantEvent;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import net.kyori.adventure.text.Component;
import net.pixlies.core.Main;
import net.pixlies.core.entity.user.User;
import net.pixlies.core.handlers.impl.PassiveHandler;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

/**
 * Passive listener to listen
 * @author Dynmie
 */
public class PassiveListener implements Listener {

    private final PassiveHandler passiveHandler = Main.getInstance().getHandlerManager().getHandler(PassiveHandler.class);

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onSuffocate(EntityInsideBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDie(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementCriterionGrantEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrame(PlayerItemFrameChangeEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (!user.isPassive()) return;
        event.setCancelled(true);
    }

    /**
     * Open chests silently.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.get(player.getUniqueId());
        Map<UUID, Container> scp = passiveHandler.getSilentChestPlayers();
        UUID uuid = player.getUniqueId();
        if (!user.isPassive()) return;
        if (!event.getAction().isRightClick()) return;

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (!(block instanceof Container container)) {
            return;
        }

        if (!player.hasPermission("pixlies.moderation.container.silent")) {
            return;
        }

        event.setCancelled(true);

        Component name = container.customName();

        Inventory inventory = Bukkit.createInventory(container.getInventory().getHolder(), container.getInventory().getSize());
        if (name != null) {
            inventory = Bukkit.createInventory(container.getInventory().getHolder(), container.getInventory().getSize(), name);
        }

        passiveHandler.getSilentChestPlayers().put(uuid, container);

        player.openInventory(inventory);
    }

    /**
     * Close chests silently
     */
    @EventHandler
    public void onSilentChestClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Map<UUID, Container> scp = passiveHandler.getSilentChestPlayers();
        Inventory inventory = event.getInventory();

        if (scp.containsKey(uuid)) {
            Container container = scp.get(uuid);
            container.getInventory().setContents(inventory.getContents());
        }

        scp.remove(uuid);
    }

}
