package net.pixlies.business.util;

import net.pixlies.business.market.Order;
import net.pixlies.business.market.OrderBook;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketUtil {
      public static BigDecimal getTaxedPrice(UUID playerUUID, double price) {
            NationProfile profile = NationProfile.get(playerUUID);
            Nation nation = Nation.getFromId(profile.getNationId());
            if (nation == null)
                  return BigDecimal.valueOf(price);
            return BigDecimal.valueOf(price).multiply(nation.getTaxRate().add(BigDecimal.valueOf(1)));
      }
      
      public static List<Order> getPlayerBuyOrders(UUID uuid) {
            List<Order> list = new ArrayList<>();
            for (OrderBook book : OrderBook.getAll()) {
                  if (book.getBuyOrders().isEmpty()) continue;
                  for (Order order : book.getBuyOrders()) {
                        if (Bukkit.getOfflinePlayer(order.getPlayerUUID()) == Bukkit.getOfflinePlayer(uuid))
                              list.add(order);
                  }
            }
            return list;
      }
      
      public static List<Order> getPlayerSellOrders(UUID uuid) {
            List<Order> list = new ArrayList<>();
            for (OrderBook book : OrderBook.getAll()) {
                  if (book.getSellOrders().isEmpty()) continue;
                  for (Order order : book.getSellOrders()) {
                        if (Bukkit.getOfflinePlayer(order.getPlayerUUID()) == Bukkit.getOfflinePlayer(uuid))
                              list.add(order);
                  }
            }
            return list;
      }
}
