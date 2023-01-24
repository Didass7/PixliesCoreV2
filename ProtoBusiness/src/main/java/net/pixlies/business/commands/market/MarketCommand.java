package net.pixlies.business.commands.market;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.PixliesEconomy;
import net.pixlies.business.guis.MarketInitialGUI;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.OrderBook;
import net.pixlies.business.market.MarketProfile;
import net.pixlies.business.util.MarketRestrictUtil;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.business.util.preconditions.CommandPreconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Market command.
 * Վե՜հ գաղափար Դաշնակցութեան։
 *
 * @author vyketype
 */
@CommandAlias("market|m|nasdaq|nyse|snp500|dowjones|ftse")
public class MarketCommand extends BaseCommand {
      /*
       * SOUNDS
       * - placed new order: block.amethyst_block.break
       * - invalid/error/restricted: block.anvil.land
       * - claimed goods/unrestricted: entity.experience_orb.pickup
       * - cancelled order: block.netherite_block.place
       */
      
      private static final PixliesEconomy instance = PixliesEconomy.getInstance();
      
      @Default
      @Description("Opens the market menu")
      @CommandPermission("pixlies.business.market")
      public void onMarket(Player player) {
            // If the market is not open
            if (!CommandPreconditions.isMarketOpen(player, MarketLang.MARKET_IS_CLOSED))
                  return;
            
            // If the player is restricted from the market
            if (CommandPreconditions.isPlayerMarketRestricted(player))
                  return;
            
            // Open the GUI
            UUID uuid = player.getUniqueId();
            MarketInitialGUI.open(uuid);
      }
      
      @Subcommand("open")
      @CommandPermission("pixlies.business.market.gates")
      @Description("Opens the market to the public")
      public void onMarketOpen(Player player) {
            // If the market was already open
            if (CommandPreconditions.isMarketAlreadyOpen(player))
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
            if (!CommandPreconditions.isMarketOpen(player, MarketLang.MARKET_WAS_ALREADY_CLOSED))
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
            OrderBook.resetAll();
            MarketLang.MARKET_STATISTICS_RESET.broadcast();
            SoundUtil.success(player);
      }
      
      @Subcommand("restrict")
      @CommandPermission("pixlies.business.market.restrict")
      @Description("Restricts/unrestricts a player from accessing the market")
      @Syntax("<target> [reason]")
      public void onMarketRestrict(Player player, String targetName) {
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
      
            // If the player has not joined before
            if (!CommandPreconditions.hasPlayerEverJoined(player, offlineTarget.getUniqueId()))
                  return;
      
            MarketProfile profile = MarketProfile.get(offlineTarget.getUniqueId());
            
            // Unrestrict/restrict
            if (profile.isRestricted()) {
                  MarketRestrictUtil.sendUnrestrictMessage(player, offlineTarget);
                  profile.setRestricted(false);
            } else {
                  MarketRestrictUtil.sendRestrictMessage(player, offlineTarget);
                  profile.setRestricted(true);
            }
      
            profile.save();
      }
      
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
