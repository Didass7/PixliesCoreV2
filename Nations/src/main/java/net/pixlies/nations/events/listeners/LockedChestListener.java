package net.pixlies.nations.events.listeners;

import net.pixlies.core.Main;
import net.pixlies.core.entity.User;
import net.pixlies.core.localization.Lang;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class LockedChestListener implements Listener {

    private static final Main instance = Main.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.CHEST) {
            if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
                User user = User.get(player.getUniqueId());

                TileState state = (TileState) block.getState();
                PersistentDataContainer container = state.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(instance, "locked-chests");
                String ownerUuid = container.getOrDefault(key, PersistentDataType.STRING, "none");

                // TODO: Nation bypass
                if (container.has(key, PersistentDataType.STRING) && ownerUuid.equals(player.getUniqueId().toString()) /* && !user.isStaffModeEnabled() */) {
                    event.setCancelled(true);
                    Lang.CHEST_BELONGS_TO_OTHER.send(player);
                    player.playSound(player.getLocation(), "block.chest.locked", 100, 1);
                }
            }
        }
    }

}
