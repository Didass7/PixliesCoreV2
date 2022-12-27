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
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.Util;
import net.pixlies.core.Main;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Market command.
 * Վե՜հ գաղափար Դաշնակցութեան։
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
                  p.sendTitle(MarketLang.MARKET + "§b§lOpen", "§7Use §a/market §7to access it.");
                  SoundUtil.grandSuccess(p);
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
                  p.sendTitle(MarketLang.MARKET + "§c§lClosed", "§7It's just business.");
                  SoundUtil.grandError(p);
            }
      }
      
      @Subcommand("resetstats")
      @CommandPermission("pixlies.business.market.resetstats")
      @Description("Resets the market statistics")
      public void onMarketReset(Player player) {
            instance.getMarketManager().resetBooks();
            MarketLang.MARKET_STATISTICS_RESET.broadcast();
            SoundUtil.success(player);
      }
      
      @Subcommand("restrict")
      @CommandPermission("pixlies.business.market.restrict")
      @Description("Restricts/unrestricts a player from accessing the market")
      @Syntax("<target> [reason]")
      public void onMarketRestrict(Player player, Player target, @Optional String arg) {
            UUID uuid = target.getUniqueId();
            MarketProfile profile = MarketProfile.get(uuid);
            
            // Get the reason
            String reason = pixlies.getConfig().getString("moderation.defaultReason", "No reason given");
            if (arg != null && !arg.isBlank()) {
                  reason = arg;
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
