package net.pixlies.business.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.handlers.impl.MarketHandler;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.market.orders.OrderProfile;
import net.pixlies.business.util.Preconditions;
import net.pixlies.business.util.Util;
import net.pixlies.core.Main;
import net.pixlies.core.ranks.Rank;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
            if (!Preconditions.isMarketOpen(player, MarketLang.MARKET_IS_CLOSED))
                  return;
            
            // If the player is restricted from the market
            if (Preconditions.isPlayerMarketRestricted(player))
                  return;
            
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
            if (Preconditions.isMarketOpen(player, MarketLang.MARKET_WAS_ALREADY_OPEN))
                  return;
            
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
            if (!Preconditions.isMarketOpen(player, MarketLang.MARKET_WAS_ALREADY_CLOSED))
                  return;
            
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
            UUID uuid = target.getUniqueId();
            MarketProfile profile = MarketProfile.get(uuid);
            
            // Get the reason
            reason = reason.substring(target.getName().length());
            if (reason.equals("")) {
                  reason = pixlies.getConfig().getString("moderation.defaultReason", "No reason given");
            }
            
            // Unrestrict/restrict
            if (profile.isRestricted()) {
                  Util.sendUnrestrictMessage(player, target, reason);
                  profile.setRestricted(false);
            } else {
                  Util.sendRestrictMessage(player, target, reason);
                  profile.setRestricted(true);
            }
            
            profile.save();
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
