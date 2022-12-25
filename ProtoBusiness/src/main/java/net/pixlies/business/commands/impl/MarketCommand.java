package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.Util;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.orders.OrderProfile;
import net.pixlies.core.Main;
import net.pixlies.core.ranks.Rank;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Market command.
 *
 * @author vyketype
 */
@CommandAlias("market|m|nasdaq|nyse|snp500|dowjones|ftse")
@CommandPermission("pixlies.business.market")
public class MarketCommand extends BaseCommand {
      /*
       * SOUNDS
       * - placed new order: block.amethyst_block.break
       * - invalid/error/restricted: block.anvil.land
       * - claimed goods/unrestricted: entity.experience_orb.pickup
       * - cancelled order: block.netherite_block.place
       */
      
      private static final Main pixlies = Main.getInstance();
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      private final MarketHandler marketHandler = instance.getHandlerManager().getHandler(MarketHandler.class);
      
      @Default
      @Description("Opens the market menu")
      public void onMarket(Player player) {
            // If the market is not open
            if (!instance.getConfig().getBoolean("marketOpen")) {
                  MarketLang.MARKET_IS_CLOSED.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return;
            }
            
            // If the player is restricted from the market
            @Nullable List<String> restricted = instance.getConfig().getStringList("restricted");
            if (!restricted.isEmpty() && restricted.contains(player.getUniqueId().toString())) {
                  MarketLang.MARKET_PLAYER_IS_RESTRICTED.send(player);
                  player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 1F);
                  return;
            }
            
            // Create the OrderProfile, open the GUI
            UUID uuid = player.getUniqueId();
            OrderProfile profile = new OrderProfile(uuid);
            marketHandler.getProfiles().put(uuid.toString(), profile);
            profile.openMarketPage();
      }
      
      @Subcommand("open")
      @CommandPermission("pixlies.business.market.gates")
      @Description("Opens the market to the public")
      public void onMarketOpen(Player player) {
            // If the market was already open
            if (instance.getConfig().getBoolean("marketOpen")) {
                  MarketLang.MARKET_WAS_ALREADY_OPEN.send(player);
                  return;
            }
            
            // Open the market gates
            instance.getConfig().set("marketOpen", true);
            MarketLang.MARKET_OPEN.broadcast();
            for (Player p : Bukkit.getOnlinePlayers()) {
                  p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100F, 0.5F);
            }
      }
      
      @Subcommand("close")
      @CommandPermission("pixlies.business.market.gates")
      @Description("Closes the market")
      public void onMarketClose(Player player) {
            // If the market was already closed
            if (!instance.getConfig().getBoolean("marketOpen")) {
                  MarketLang.MARKET_WAS_ALREADY_CLOSED.send(player);
                  return;
            }
            
            // Close the market gates
            instance.getConfig().set("marketOpen", false);
            MarketLang.MARKET_CLOSED.broadcast();
            for (Player p : Bukkit.getOnlinePlayers()) {
                  p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 100F, 0.5F);
            }
      }
      
      @Subcommand("resetstats")
      @CommandPermission("pixlies.business.market.resetstats")
      @Description("Resets the market statistics")
      public void onMarketReset(Player player, @Optional Player target) {
            // If we should reset general market stats
            if (target == null) {
                  instance.getMarketManager().resetBooks();
                  MarketLang.MARKET_STATISTICS_RESET.broadcast();
                  player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 1F);
                  return;
            }
            
            // Reset statistics
            instance.getMarketManager().resetPlayer(target);
            
            // If player is online
            if (target.isOnline()) {
                  MarketLang.MARKET_PLAYER_STATISTICS_RESET.send(
                          target,
                          "%PLAYER%;" + Rank.getRank(target.getUniqueId()).getColor() + target.getName(),
                          "%SENDER%;" + Rank.getRank(player.getUniqueId()).getColor() + player.getName()
                  );
                  target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100F, 1F);
            }
      }
      
      @Subcommand("restrict")
      @CommandPermission("pixlies.business.market.restrict")
      @Description("Restricts/unrestricts a player from accessing the market")
      public void onMarketRestrict(Player player, Player target, @Optional String reason) {
            String uuid = target.getUniqueId().toString();
            List<String> restricted = instance.getConfig().getStringList("restricted");
            
            // Get the reason
            reason = reason.substring(target.getName().length());
            if (reason.equals("")) {
                  reason = pixlies.getConfig().getString("moderation.defaultReason", "No reason given");
            }
            
            // Unrestrict/restrict
            if (restricted.contains(uuid)) {
                  Util.sendRestrictMessage(player, target, reason);
                  restricted.remove(uuid);
            } else {
                  Util.sendUnrestrictMessage(player, target, reason);
                  restricted.add(uuid);
            }
            
            // Update the list
            instance.getConfig().set("restricted", restricted);
      }
      
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
      
      @Getter
      @AllArgsConstructor
      public enum Selection {
            MINERALS(Material.DIAMOND_PICKAXE, "§b", false, true, false),
            FOODSTUFFS_AND_PLANTS(Material.GOLDEN_HOE, "§e", true, true, true),
            BLOCKS(Material.IRON_SHOVEL, "§d", true, true, true),
            MOB_DROPS(Material.NETHERITE_SWORD, "§c", false, true, false),
            MISCELLANEOUS(Material.ARROW, "§6", false, false, false);
            
            private final Material material;
            private final String color;
            private final boolean seventhColumn;
            private final boolean fourthRow;
            private final boolean fifthRow;
            
            public boolean hasSeventhColumn() {
                  return seventhColumn;
            }
            
            public boolean hasFourthRow() {
                  return fourthRow;
            }
            
            public boolean hasFifthRow() {
                  return fifthRow;
            }
            
            public String getName() {
                  return WordUtils.capitalize(name().toLowerCase().replace("_", " "));
            }
      }
}
