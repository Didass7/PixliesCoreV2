package net.pixlies.business.util;

import net.pixlies.business.market.orders.Order;
import net.pixlies.business.market.orders.OrderBook;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MarketUtil {
      public static List<Order> getPlayerBuyOrders(UUID uuid) {
            List<Order> list = new LinkedList<>();
            for (OrderBook book : OrderBook.getAll()) {
                  if (book.getBuyOrders() != null) {
                        for (Order order : book.getBuyOrders()) {
                              if (order.getPlayerUUID() == uuid) list.add(order);
                        }
                  }
            }
            return list;
      }
      
      public static List<Order> getPlayerSellOrders(UUID uuid) {
            List<Order> list = new LinkedList<>();
            for (OrderBook book : OrderBook.getAll()) {
                  if (book.getSellOrders() != null) {
                        for (Order order : book.getSellOrders()) {
                              if (order.getPlayerUUID() == uuid) list.add(order);
                        }
                  }
            }
            return list;
      }
}
