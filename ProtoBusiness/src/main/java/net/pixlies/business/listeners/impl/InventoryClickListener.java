package net.pixlies.business.listeners.impl;

import net.pixlies.business.guis.OrderItemGUI;
import net.pixlies.business.market.OrderItem;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryClickListener implements Listener {
      public static List<UUID> VIEWING_MARKET = new ArrayList<>();
      
      @EventHandler
      public void onInventoryClick(InventoryClickEvent event) {
            HumanEntity entity = event.getWhoClicked();
            if (!(entity instanceof Player player))
                  return;
      
            if (!VIEWING_MARKET.contains(player.getUniqueId()))
                  return;
            
            ItemStack itemStack = event.getInventory().getItem(event.getSlot());
            if (itemStack == null || itemStack.getType() == Material.AIR)
                  return;
            
            OrderItem orderItem = OrderItem.getFromMaterial(itemStack.getType());
            if (orderItem == null)
                  return;
            
            player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
            OrderItemGUI.open(player.getUniqueId(), orderItem);
      }
}
