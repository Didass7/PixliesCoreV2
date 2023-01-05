package net.pixlies.business.listeners.impl;

import net.pixlies.business.market.profiles.OrderProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        
        if (!OrderProfile.hasProfile(player.getUniqueId()))
            return;
    
        InventoryCloseEvent.Reason reason = event.getReason();
        if (reason != InventoryCloseEvent.Reason.PLUGIN && reason != InventoryCloseEvent.Reason.OPEN_NEW) {
            OrderProfile.get(player.getUniqueId()).remove();
        }
    }
}
