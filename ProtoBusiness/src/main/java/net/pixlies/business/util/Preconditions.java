package net.pixlies.business.util;

import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.nations.Tariff;
import net.pixlies.business.market.orders.OrderItem;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationPermission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class Preconditions {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      public static boolean doesPageExist(Player player, int page, int pages) {
            if (page > pages || page < 1) {
                  MarketLang.PLAYER_DOES_NOT_EXIST.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean doesPlayerExist(Player player, String name) {
            if (Bukkit.getPlayerExact(name) == null) {
                  MarketLang.PLAYER_DOES_NOT_EXIST.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isPlayerHoldingAir(Player player, Material material) {
            if (material == Material.AIR) {
                  MarketLang.PRICE_NOT_HOLDING_AN_ITEM.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
      }
      
      public static boolean doesMarketItemExist(Player player, OrderItem item) {
            if (item == null) {
                  MarketLang.ITEM_NOT_ON_MARKET.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isMarketOpen(Player player, MarketLang message) {
            if (!instance.getConfig().getBoolean("marketOpen")) {
                  message.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
      }
      
      public static boolean isPlayerMarketRestricted(Player player) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            if (profile.isRestricted()) {
                  MarketLang.MARKET_PLAYER_IS_RESTRICTED.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
      }
      
      public static boolean isPlayerInNation(Player player, NationProfile profile) {
            if (!profile.isInNation()) {
                  NationsLang.NOT_IN_NATION.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean ifNoLocalTariffs(Player player, List<Tariff> incoming, List<Tariff> outgoing) {
            if (incoming.isEmpty() && outgoing.isEmpty()) {
                  MarketLang.NO_TARIFFS_FOUND.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
     }
     
     public static boolean ifNoGlobalTariffs(Player player) {
           if (Tariff.getAll().isEmpty()) {
                 MarketLang.NO_TARIFFS_FOUND.send(player);
                 SoundUtil.error(player);
                 return true;
           }
           return false;
     }
     
     public static boolean isTariffTypeValid(Player player, String type) {
            if (!type.equalsIgnoreCase("imports") && !type.equalsIgnoreCase("exports")) {
                  MarketLang.TARIFF_TYPE_NOT_VALID.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
     }
     
     public static boolean isRateANumber(Player player, String rate) {
           try {
                 Double.parseDouble(rate);
           } catch (NullPointerException | NumberFormatException e) {
                 double maxRate = instance.getConfig().getDouble("tariffMaxRate");
                 MarketLang.TARIFF_RATE_NOT_VALID.send(player, "%MAX%;" + maxRate);
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
     
     public static boolean tariffGeneral(Player player, String targetNation) {
           User user = User.get(player.getUniqueId());
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           @Nullable Nation target = Nation.getFromName(targetNation);
      
           boolean notInNation = !nationProfile.isInNation();
           boolean noPermission = !NationPermission.MANAGE_TARIFFS.hasPermission(player) && !user.isBypassing();
           boolean nationNull = target == null;
      
           if (notInNation) NationsLang.NOT_IN_NATION.send(player);
           if (noPermission) NationsLang.NATION_NO_PERMISSION.send(player);
           if (nationNull) NationsLang.NATION_DOES_NOT_EXIST.send(player);
      
           if (notInNation || noPermission || nationNull) {
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
     
     public static boolean tariffSet(Player player, String[] args) {
           String targetNation = args[0];
           String tariffType = args[1];
           String strRate = args[2];
      
           if (!Preconditions.tariffGeneral(player, targetNation))
                 return false;
           
           double maxRate = instance.getConfig().getDouble("tariffMaxRate");
           double rate = Double.parseDouble(strRate);
      
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           boolean rateNotValid = rate > maxRate || rate < 0.01;
           boolean tariffExists = false;
           
           @Nullable String tariffId = Tariff.getTariffId(
                   nationProfile.getNation().getName(),
                   targetNation,
                   Tariff.Type.valueOf(tariffType.toUpperCase())
           );
           
           if (tariffId != null) {
                 if (Tariff.get(tariffId).getType().toString().equalsIgnoreCase(tariffType)) {
                       tariffExists = true;
                 }
           }
           
           if (rateNotValid) MarketLang.TARIFF_RATE_NOT_VALID.send(player, "%MAX%;" + maxRate);
           if (tariffExists) MarketLang.TARIFF_ALREADY_EXISTS.send(player);
      
           if (rateNotValid || tariffExists) {
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
     
     public static boolean tariffRemove(Player player, String[] args) {
           String targetNation = args[0];
           String tariffType = args[1];
      
           if (!Preconditions.tariffGeneral(player, targetNation))
                 return false;
           
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           boolean tariffNull = true;
      
           @Nullable String tariffId = Tariff.getTariffId(
                   nationProfile.getNation().getName(),
                   targetNation,
                   Tariff.Type.valueOf(tariffType.toUpperCase())
           );
           
           if (tariffId != null) {
                 if (Tariff.get(tariffId).getType().toString().equalsIgnoreCase(tariffType)) {
                       tariffNull = false;
                 }
           }

           if (tariffNull) {
                 MarketLang.TARIFF_DOES_NOT_EXIST.send(player);
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
     
     public static boolean isTariffDeleteSuccessful(Player player, Tariff tariff) {
           if (!tariff.delete()) {
                 MarketLang.TARIFF_FAILED_TO_REMOVE.send(player);
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
}
