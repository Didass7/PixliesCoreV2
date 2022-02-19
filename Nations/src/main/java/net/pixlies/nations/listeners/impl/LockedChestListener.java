package net.pixlies.nations.listeners.impl;

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
        User user = User.get(player.getUniqueId());

        if (block == null || block.getType() != Material.CHEST) return;
        if (!(block instanceof TileState state)) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        NamespacedKey key = new NamespacedKey(instance, "nations:locked-chests");
        PersistentDataContainer container = state.getPersistentDataContainer();

        if (!container.has(key, PersistentDataType.PrimitivePersistentDataType.STRING)) return;

        String ownerUuid = container.get(key, PersistentDataType.PrimitivePersistentDataType.STRING);
        if (ownerUuid == null) return;

        if (ownerUuid.equals(player.getUniqueId().toString()) || !(user.getSettings().isBypassing() && player.hasPermission("pixlies.staff.lockedchests"))) return;

        player.playSound(player.getLocation(), "block.chest.locked", Float.MAX_VALUE, 1);
        Lang.CHEST_BELONGS_TO_OTHER.send(player);
        event.setCancelled(true);

    }

}
