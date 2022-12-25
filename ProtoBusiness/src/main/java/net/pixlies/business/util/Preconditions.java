package net.pixlies.business.util;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.business.market.orders.Tariff;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class Preconditions {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      public static boolean doesPlayerExist(Player player, String name) {
            if (Bukkit.getPlayerExact(name) == null) {
                  MarketLang.PLAYER_DOES_NOT_EXIST.send(player);
                  player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100F, 0.5F);
                  return false;
            }
            return true;
      }
      
      public static boolean isPlayerHoldingAir(Player player, Material material) {
            if (material == Material.AIR) {
                  MarketLang.PRICE_NOT_HOLDING_AN_ITEM.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return true;
            }
            return false;
      }
      
      public static boolean doesMarketItemExist(Player player, OrderItem item) {
            if (item == null) {
                  MarketLang.ITEM_NOT_ON_MARKET.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return false;
            }
            return true;
      }
      
      public static boolean isMarketOpen(Player player, MarketLang message) {
            if (instance.getConfig().getBoolean("marketOpen")) {
                  message.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return true;
            }
            return false;
      }
      
      public static boolean isPlayerMarketRestricted(Player player) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            if (profile.isRestricted()) {
                  MarketLang.MARKET_PLAYER_IS_RESTRICTED.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return true;
            }
            return false;
      }
      
      public static boolean isPlayerInNation(Player player, NationProfile profile) {
            if (!profile.isInNation()) {
                  NationsLang.NOT_IN_NATION.send(player);
                  player.playSound(player.getLocation(), "block.anvil.land", 100F, 1F);
                  return false;
            }
            return true;
      }
      
      public static boolean ifNoLocalTariffs(Player player, List<Tariff> incoming, List<Tariff> outgoing) {
            if (incoming.isEmpty() && outgoing.isEmpty()) {
                  MarketLang.NO_TARIFFS_FOUND.send(player);
                  player.playSound(player.getLocation(), "block.anvil.land", 100F, 1F);
                  return true;
            }
            return false;
     }
     
     public static boolean ifNoGlobalTariffs(Player player) {
           if (instance.getMarketManager().getTariffs().isEmpty()) {
                 MarketLang.NO_TARIFFS_FOUND.send(player);
                 player.playSound(player.getLocation(), "block.anvil.land", 100, 1);
                 return true;
           }
           return false;
     }
}
