package net.pixlies.business.util;

import net.pixlies.business.market.OrderItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Objects;
import java.util.UUID;

public class InventoryUtil {
      public static int getItemAmount(UUID uuid, OrderItem item) {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            Inventory inv = player.getInventory();
            int num = 0;
            for (int i = 0; i < inv.getSize(); i++) {
                  if (inv.getItem(i) == null) continue;
                  if (Objects.equals(Objects.requireNonNull(inv.getItem(i)).getType(), item.getMaterial())) {
                        num += Objects.requireNonNull(inv.getItem(i)).getAmount();
                  }
            }
            return num;
      }
}
