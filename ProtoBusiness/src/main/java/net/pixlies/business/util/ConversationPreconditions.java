package net.pixlies.business.util;

import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;

public class ConversationPreconditions {
      public static boolean playerHasEnoughMoney(Player player, double amount) {
            if (NationProfile.get(player.getUniqueId()).getBalance() < amount) {
                  MarketLang.GENERAL_NOT_ENOUGH_MONEY.send(player);
                  SoundUtil.cancelledOrder(player);
                  return false;
            }
            return true;
      }
      
      public static boolean playerHasEnoughItems(Player player, OrderItem item, int amount) {
            if (amount > InventoryUtil.getItemAmount(player.getUniqueId(), item)) {
                  MarketLang.MARKET_NOT_ENOUGH_ITEMS.send(player);
                  SoundUtil.cancelledOrder(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isPositiveInteger(Player player, Number input) {
            if (input.doubleValue() % 1 != 0 || input.intValue() <= 0) {
                  MarketLang.THIS_IS_NOT_A_POSITIVE_INTEGER.get(player);
                  SoundUtil.cancelledOrder(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isPositiveDouble(Player player, Number input) {
            if (input.doubleValue() <= 0) {
                  MarketLang.THIS_IS_NOT_A_POSITIVE_INTEGER.get(player);
                  SoundUtil.cancelledOrder(player);
                  return false;
            }
            return true;
      }
}
