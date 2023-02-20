package net.pixlies.business.market;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.util.InventoryUtil;
import net.pixlies.nations.nations.interfaces.NationProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

/**
 * Market profile.
 *
 * @author vyketype
 */
@Getter
@Setter
public class MarketProfile {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      private static final String PROFILES_PATH = instance.getDataFolder().getAbsolutePath() + "/profiles/";
      private static final Map<UUID, MarketProfile> CACHE = new HashMap<>();
      
      private final UUID uuid;
      
      @Setter(AccessLevel.PRIVATE)
      private List<UUID> blockedPlayers;
      
      private boolean restricted;
      
      @Getter(AccessLevel.NONE)
      private boolean hasJoinedBefore;
      
      private int buyOrdersMade;
      private int sellOrdersMade;
      private int tradesMade;
      private BigDecimal moneySpent;
      private BigDecimal moneyGained;
      private int itemsSold;
      private int itemsBought;
      
      public MarketProfile(UUID uuid) {
            this.uuid = uuid;
            blockedPlayers = new ArrayList<>();
            restricted = false;
            hasJoinedBefore = true;
            buyOrdersMade = 0;
            sellOrdersMade = 0;
            tradesMade = 0;
            moneySpent = BigDecimal.valueOf(0.0);
            moneyGained = BigDecimal.valueOf(0.0);
            itemsSold = 0;
            itemsBought = 0;
      }
      
      public boolean hasJoinedBefore() {
            return hasJoinedBefore;
      }
      
      public void tradeBlockPlayer(UUID uuid) {
            blockedPlayers.add(uuid);
      }
      
      public void unTradeBlockPlayer(UUID uuid) {
            blockedPlayers.remove(uuid);
      }
      
      public void addBuy() {
            buyOrdersMade += 1;
      }
      
      public void addSell() {
            sellOrdersMade += 1;
      }
      
      public void addTrade() {
            tradesMade += 1;
      }
      
      public void addMoneySpent(BigDecimal money) {
            moneySpent = moneySpent.add(money);
      }
      
      public void addMoneyGained(BigDecimal money) {
            moneyGained = moneyGained.add(money);
      }
      
      public void addItemsSold(int items) {
            itemsSold += items;
      }
      
      public void addItemsBought(int items) {
            itemsBought += items;
      }
      
      public void sendNotification() {
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            
            if (player.isOnline()) {
                  MarketLang.MARKET_NOTIFICATION.send(player);
                  player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 1F);
            }
      }
      
      // Will give all non-claimed goods and refund excess money on order claim
      public void claimGoods(Order order) {
            NationProfile profile = NationProfile.get(uuid);
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            
            OrderBook book = OrderBook.get(order.getBookItem());
            Material material = book.getItem().getMaterial();
            
            if (order.getType() == Order.Type.BUY) {
                  int amount = 0;
                  for (Trade trade : order.getTrades()) {
                        if (trade.isClaimed()) continue;
                        amount += trade.getAmount();
                        trade.claim();
                        trade.save();
                  }
                  
                  InventoryUtil.addItemsToInventory(player, new ItemStack(material, amount));
                  order.refundPlayer();
                  
                  MarketLang.ORDER_ITEMS_CLAIMED.send(player, "%AMOUNT%;" + amount, "%ITEM%;" + book.getItem().getName());
                  MarketLang.ORDER_UNUSED_COINS_REFUNDED.send(player, "%COINS%;" + order.getRefundableCoins());
            } else {
                  BigDecimal coins = BigDecimal.valueOf(0.0);
                  for (Trade trade : order.getTrades()) {
                        if (trade.isClaimed()) continue;
                        coins = coins.add(trade.getPrice().multiply(BigDecimal.valueOf(trade.getAmount())));
                        trade.claim();
                        trade.save();
                  }

                  profile.addBalance(coins);
                  profile.save();
                  
                  MarketLang.ORDER_ITEMS_CLAIMED.send(player, "%COINS%;" + coins);
            }
      }
      
      // Will refund all non-sold / non-bought goods on cancellation
      public void refundGoods(Order order) {
            NationProfile profile = NationProfile.get(uuid);
            Player player = Bukkit.getPlayer(uuid);
            assert player != null;
            
            if (order.getType() == Order.Type.BUY) {
                  BigDecimal amount = order.getTaxedPrice().multiply(BigDecimal.valueOf(order.getVolume()));
                  MarketLang.ORDER_COINS_REFUNDED.send(player, "%COINS%;" + amount);
                  profile.addBalance(amount);
                  profile.save();
            } else {
                  OrderBook book = OrderBook.get(order.getBookItem());
                  Material material = book.getItem().getMaterial();
                  InventoryUtil.addItemsToInventory(player, new ItemStack(material, order.getVolume()));
                  MarketLang.ORDER_ITEMS_REFUNDED.send(
                          player,
                          "%AMOUNT%;" + order.getVolume(),
                          "%ITEM%;" + book.getItem().getName()
                  );
            }
      }
      
      public void save() {
            CACHE.put(uuid, this);
            String playerName = Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
            instance.logInfo("The MarketProfile of " + playerName + " has been saved to the CACHE.");
      }
      
      public void backup() {
            String filename = uuid.toString() + ".yml";
      
            File file = new File(PROFILES_PATH + filename);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            
            List<String> blockedList = new ArrayList<>();
            for (UUID uuid : blockedPlayers) {
                  blockedList.add(uuid.toString());
            }
      
            yaml.set("blockedPlayers", blockedList);
            yaml.set("restricted", restricted);
            yaml.set("hasJoinedBefore", hasJoinedBefore);
            yaml.set("buyOrdersMade", buyOrdersMade);
            yaml.set("sellOrdersMade", sellOrdersMade);
            yaml.set("tradesMade", tradesMade);
            yaml.set("moneySpent", moneySpent.doubleValue());
            yaml.set("moneyGained", moneyGained.doubleValue());
            yaml.set("itemsSold", itemsSold);
            yaml.set("itemsBought", itemsBought);
            
            try {
                  yaml.save(file);
            } catch (IOException ex) {
                  ex.printStackTrace();
                  instance.getLogger().log(Level.SEVERE, "Unable to save MarketProfile of " + uuid + ".");
            }
      
            String playerName = Bukkit.getOfflinePlayer(uuid).getName();
            instance.logInfo("The MarketProfile of " + playerName + " has been backed up to the files.");
      }
      
      // --------------------------------------------------------------------------------------------
      
      public static void backupAll() {
            CACHE.values().forEach(MarketProfile::backup);
      }
      
      private static MarketProfile getFromFiles(UUID uuid) {
            String filename = uuid.toString() + ".yml";
            
            File file = new File(PROFILES_PATH + filename);
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            
            List<UUID> blockedPlayers = new ArrayList<>();
            for (String string : yaml.getStringList("blockedPlayers")) {
                  blockedPlayers.add(UUID.fromString(string));
            }
            
            MarketProfile profile = new MarketProfile(uuid);
            profile.setRestricted(yaml.getBoolean("restricted"));
            profile.setHasJoinedBefore(yaml.getBoolean("hasJoinedBefore"));
            profile.setBlockedPlayers(blockedPlayers);
            profile.setBuyOrdersMade(yaml.getInt("buyOrdersMade"));
            profile.setSellOrdersMade(yaml.getInt("sellOrdersMade"));
            profile.setTradesMade(yaml.getInt("tradesMade"));
            profile.setMoneySpent(BigDecimal.valueOf(yaml.getDouble("moneySpent")));
            profile.setMoneyGained(BigDecimal.valueOf(yaml.getDouble("moneyGained")));
            profile.setItemsSold(yaml.getInt("itemsSold"));
            profile.setItemsBought(yaml.getInt("itemsBought"));
            
            profile.save();
            return profile;
      }
      
      public static MarketProfile get(UUID uuid) {
            // Get from CACHE
            if (CACHE.containsKey(uuid)) {
                  return CACHE.get(uuid);
            }
            
            // If the MarketProfile does not exist
            String filename = uuid.toString() + ".yml";
            if (!new File(PROFILES_PATH + filename).exists()) {
                  MarketProfile profile = new MarketProfile(uuid);
                  profile.save();
                  return profile;
            }
      
            // Get from files
            MarketProfile profile = getFromFiles(uuid);
            profile.save();
            return profile;
      }
}
