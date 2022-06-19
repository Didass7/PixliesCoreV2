package net.pixlies.business.listeners.impl;

import net.pixlies.business.market.orders.OrderProfile;
import net.pixlies.core.entity.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        User user = User.get(player.getUniqueId());
        if (OrderProfile.hasProfile(user)) {
            user.getExtras().remove("orderProfile");
            user.save();
        }
    }

}
