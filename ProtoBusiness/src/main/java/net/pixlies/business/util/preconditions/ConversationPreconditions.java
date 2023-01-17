package net.pixlies.business.util.preconditions;

import net.pixlies.business.market.OrderItem;
import net.pixlies.business.util.InventoryUtil;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.entity.Player;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class ConversationPreconditions {
      public static boolean playerHasEnoughMoney(Player player, double amount) {
            if (NationProfile.get(player.getUniqueId()).getBalance() < amount) {
                  // MarketLang.GENERAL_NOT_ENOUGH_MONEY.send(player);
                  player.sendTitle("§c§lNot enough money!", "§7You're poor.");
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean playerHasEnoughItems(Player player, OrderItem item, int amount) {
            if (amount > InventoryUtil.getItemAmount(player.getUniqueId(), item)) {
                  // MarketLang.MARKET_NOT_ENOUGH_ITEMS.send(player);
                  player.sendTitle("§c§lNot enough items!", "§7You're poor.");
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isPositiveInteger(Player player, Number input) {
            if (input.doubleValue() % 1 != 0 || input.intValue() <= 0) {
                  // MarketLang.THIS_IS_NOT_A_POSITIVE_INTEGER.get(player);
                  player.sendTitle("§c§lInvalid number!", "§7Must be a positive integer.");
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isPositiveDouble(Player player, Number input) {
            if (input.doubleValue() <= 0) {
                  // MarketLang.THIS_IS_NOT_A_POSITIVE_INTEGER.get(player);
                  player.sendTitle("§c§lInvalid number!", "§7Must be a positive number.");
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
}
