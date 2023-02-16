package net.pixlies.business.util.preconditions;

import net.pixlies.business.ProtoBusinesss;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.Embargo;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.OrderItem;
import net.pixlies.business.market.Tariff;
import net.pixlies.business.util.InventoryUtil;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.core.entity.user.User;
import net.pixlies.nations.locale.NationsLang;
import net.pixlies.nations.nations.Nation;
import net.pixlies.nations.nations.interfaces.NationProfile;
import net.pixlies.nations.nations.ranks.NationPermission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class CommandPreconditions {
      private static final ProtoBusinesss instance = ProtoBusinesss.getInstance();
      
      public static boolean doesPageExist(Player player, int page, int pages) {
            if (page > pages || page < 1) {
                  MarketLang.PLAYER_DOES_NOT_EXIST.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean playerHasEnoughItems(Player player, OrderItem item, int amount) {
            if (amount > InventoryUtil.getItemAmount(player.getUniqueId(), item)) {
                  MarketLang.MARKET_NOT_ENOUGH_ITEMS.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean playerHasEnoughMoney(Player player, double amount) {
            double balance = NationProfile.get(player.getUniqueId()).getBalance();
            if (balance < amount || balance < 0) {
                  MarketLang.GENERAL_NOT_ENOUGH_MONEY.send(player);
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
      
      public static boolean hasPlayerEverJoined(Player player, UUID uuid) {
            if (!MarketProfile.get(uuid).hasJoinedBefore()) {
                  MarketLang.PLAYER_HAS_NEVER_JOINED.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean isMarketAlreadyOpen(Player player) {
            if (instance.getConfig().getBoolean("marketOpen")) {
                  MarketLang.MARKET_WAS_ALREADY_OPEN.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
      }
      
      public static boolean isMarketOpen(Player player, MarketLang message) {
            if (!instance.getConfig().getBoolean("marketOpen")) {
                  message.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
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
      
      public static boolean doesPlayerHaveTaxPermission(Player player) {
            User user = User.get(player.getUniqueId());
            if (!NationPermission.MANAGE_TARIFFS.hasPermission(player) && !user.isBypassing()) {
                  NationsLang.NATION_NO_PERMISSION.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
      }
      
      public static boolean taxSet(Player player, NationProfile profile, String strDecimalRate) {
            // If the player is not in a nation
            if (!isPlayerInNation(player, profile))
                  return false;
      
            // If the player does not have permission
            if (!doesPlayerHaveTaxPermission(player))
                  return false;
            
            // If the rate is not a decimal number
            return isRateValidDecimal(player, strDecimalRate);
      }
      
      public static boolean ifNoLocalTariffs(Player player, List<Tariff> incoming, List<Tariff> outgoing) {
            if (incoming.isEmpty() && outgoing.isEmpty()) {
                  MarketLang.NO_TARIFFS_FOUND.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
     }
      
      public static boolean ifNoLocalEmbargoes(Player player, List<Embargo> incoming, List<Embargo> outgoing) {
            if (incoming.isEmpty() && outgoing.isEmpty()) {
                  MarketLang.NO_EMBARGOES_FOUND.send(player);
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
     
      public static boolean ifNoGlobalEmbargoes(Player player) {
            if (Embargo.getAll().isEmpty()) {
                  MarketLang.NO_EMBARGOES_FOUND.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
      }
      
     public static int getValidDuration(Player player, String strDuration) {
           try {
                 Integer.parseInt(strDuration);
           } catch (NullPointerException | NumberFormatException e) {
                 MarketLang.EMBARGO_NOT_VALID_DURATION.send(player);
                 SoundUtil.error(player);
                 return -1;
           }
           return Integer.parseInt(strDuration);
     }
     
     public static boolean isRateValidDecimal(Player player, String strDecimalRate) {
            try {
                  Double.parseDouble(strDecimalRate);
            } catch (NullPointerException | NumberFormatException e) {
                  MarketLang.MUST_BE_POSITIVE_DECIMAL.send(player);
                  SoundUtil.error(player);
                  return false;
            }
            return true;
     }
     
     public static boolean isAmountValidNumber(Player player, String amount) {
           try {
                 Double.parseDouble(amount);
           } catch (NullPointerException | NumberFormatException e) {
                 double maxRate = instance.getConfig().getDouble("tariffMaxRate");
                 MarketLang.TARIFF_RATE_NOT_VALID.send(player, "%MAX%;" + maxRate);
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
     
     private static boolean nationGeneral(Player player, String targetNation, boolean tariff) {
           User user = User.get(player.getUniqueId());
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           @Nullable Nation target = Nation.getFromName(targetNation);
      
           boolean notInNation = !nationProfile.isInNation();
           
           boolean noPermission;
           if (tariff) {
                 noPermission = !NationPermission.MANAGE_TARIFFS.hasPermission(player) && !user.isBypassing();
           } else {
                 noPermission = !NationPermission.MANAGE_EMBARGOES.hasPermission(player) && !user.isBypassing();
           }
           
           boolean nationNull = target == null;
           boolean sameNation = Objects.equals(nationProfile.getNation().getName(), targetNation);
      
           if (notInNation) NationsLang.NOT_IN_NATION.send(player);
           if (noPermission) NationsLang.NATION_NO_PERMISSION.send(player);
           if (nationNull) NationsLang.NATION_DOES_NOT_EXIST.send(player);
           if (sameNation) NationsLang.CANNOT_BE_YOUR_NATION.send(player);
      
           if (notInNation || noPermission || nationNull || sameNation) {
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
      
      public static boolean embargoRemove(Player player, String targetNation) {
            if (!CommandPreconditions.nationGeneral(player, targetNation, false))
                  return false;
            
            NationProfile nationProfile = NationProfile.get(player.getUniqueId());
            if (Embargo.getEmbargoId(nationProfile.getNation().getName(), targetNation) == null) {
                  MarketLang.EMBARGO_DOES_NOT_EXIST.send(player);
                  SoundUtil.error(player);
            }
            
            return true;
      }
     
     public static boolean embargoSet(Player player, String targetNation) {
           if (!CommandPreconditions.nationGeneral(player, targetNation, false))
                 return false;
      
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           if (Embargo.getEmbargoId(nationProfile.getNation().getName(), targetNation) != null) {
                 MarketLang.EMBARGO_ALREADY_EXISTS.send(player);
                 SoundUtil.error(player);
           }
           
           return true;
     }
     
     public static boolean tariffSet(Player player, String targetNation, String strRate) {
           if (!CommandPreconditions.nationGeneral(player, targetNation, true))
                 return false;
           
           double maxRate = instance.getConfig().getDouble("tariffMaxRate");
           double rate = Double.parseDouble(strRate);
      
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           boolean rateNotValid = rate > maxRate || rate < 0.01;
           boolean tariffExists = Tariff.getTariffId(nationProfile.getNation().getName(), targetNation) != null;
           
           if (rateNotValid) MarketLang.TARIFF_RATE_NOT_VALID.send(player, "%MAX%;" + maxRate);
           if (tariffExists) MarketLang.TARIFF_ALREADY_EXISTS.send(player);
      
           if (rateNotValid || tariffExists) {
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
     
     public static boolean tariffRemove(Player player, String targetNation) {
           if (!CommandPreconditions.nationGeneral(player, targetNation, true))
                 return false;
           
           NationProfile nationProfile = NationProfile.get(player.getUniqueId());
           boolean tariffNull = Tariff.getTariffId(nationProfile.getNation().getName(), targetNation) == null;

           if (tariffNull) {
                 MarketLang.TARIFF_DOES_NOT_EXIST.send(player);
                 SoundUtil.error(player);
                 return false;
           }
           return true;
     }
      
      public static boolean isEmbargoDeleteSuccessful(Player player, Embargo embargo) {
            if (!embargo.delete()) {
                  MarketLang.EMBARGO_FAILED_TO_REMOVE.send(player);
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
      
      public static boolean isPlayerAlreadyTradeBlocked(Player player, String name) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (profile.getBlockedPlayers().contains(uuid)) {
                  MarketLang.PLAYER_IS_TRADEBLOCKED.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
      }
      
     public static boolean isPlayerAlreadyNotTradeBlocked(Player player, String name) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (!profile.getBlockedPlayers().contains(uuid)) {
                  MarketLang.PLAYER_IS_NOT_TRADEBLOCKED.send(player);
                  SoundUtil.error(player);
                  return true;
            }
            return false;
     }
     
     public static boolean economy(Player player, String strArgs) {
           String[] args = StringUtils.split(strArgs, " ", -1);
           String targetName = args[0];
           String strAmount = args[1];
      
           OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
      
           // If the player has not joined before
           if (!CommandPreconditions.hasPlayerEverJoined(player, offlineTarget.getUniqueId()))
                 return false;
      
           // If the amount is a valid number
           return CommandPreconditions.isAmountValidNumber(player, strAmount);
     }
}
