package net.pixlies.business.listeners.impl;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
      @EventHandler
      public void onInventorClose(InventoryCloseEvent event) {
            HumanEntity entity = event.getPlayer();
            if (!(entity instanceof Player player))
                  return;
            
            boolean cannotUse = event.getReason().equals(InventoryCloseEvent.Reason.CANT_USE);
            boolean openedNew = event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW);
            if (!cannotUse && !openedNew) {
                  InventoryClickListener.VIEWING_MARKET.remove(player.getUniqueId());
            }
      }
}
