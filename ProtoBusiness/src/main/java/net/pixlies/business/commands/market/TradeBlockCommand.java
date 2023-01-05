package net.pixlies.business.commands.market;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import net.pixlies.business.ProtoBusiness;
import net.pixlies.business.locale.MarketLang;
import net.pixlies.business.market.profiles.MarketProfile;
import net.pixlies.business.util.Preconditions;
import net.pixlies.business.util.SoundUtil;
import net.pixlies.core.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * TradeBlock command.
 * ՄԻԱՅՆ ԶԷՆՔՈՎ ԿԱՅ ՀԱՅՈՂ ՓՐԿՈՒԹԻՒՆ։
 * ԿԵՑՑԷ՛ Հ․Յ․Դաշնակցութիւնը։
 *
 * @author vyketype
 */
@CommandAlias("tradeblock")
@CommandPermission("pixlies.business.tradeblock")
public class TradeBlockCommand extends BaseCommand {
      private static final ProtoBusiness instance = ProtoBusiness.getInstance();
      
      @Subcommand("list")
      @Description("View who you have blocked")
      public void onTradeBlockList(Player player) {
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            List<UUID> list = profile.getBlockedPlayers();
      
            // If there are no trade-blocked players
            if (list.isEmpty()) {
                  MarketLang.TRADE_BLOCK_LIST_EMPTY.send(player);
                  return;
            }
            
            // Format names
            StringBuilder names = new StringBuilder();
            for (UUID uuid : list) {
                  String name = Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();
                  names.append(Rank.getRank(uuid).getColor()).append(name);
                  if (list.indexOf(uuid) != list.size() - 1) {
                        names.append(ChatColor.translateAlternateColorCodes('§', ","));
                  }
            }
            
            // Send message
            MarketLang.TRADE_BLOCK_LIST.send(player, "%LIST%;" + names);
      }
      
      @Subcommand("add")
      @Description("Block a player from trading with you")
      @Syntax("<player>")
      public void onTradeBlockAdd(Player player, String name) {
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(name);
            
            // If the player has not joined before
            if (!Preconditions.hasPlayerEverJoined(player, offlineTarget.getUniqueId()))
                  return;
            
            // If the player is already trade-blocked
            if (Preconditions.isPlayerAlreadyTradeBlocked(player, name))
                  return;
            
            // Block player
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            profile.tradeBlockPlayer(offlineTarget.getUniqueId());
            profile.save();
            
            // Send messages and play sound
            MarketLang.TRADE_BLOCK_ADDED.send(
                    player,
                    "%PLAYER%;" + Rank.getRank(offlineTarget.getUniqueId()).getColor() + name
            );
            SoundUtil.success(player);
      }
      
      @Subcommand("remove")
      @Description("Unblock a player from trading with you")
      @Syntax("<player>")
      public void onTradeBlockRemove(Player player, String name) {
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(name);
      
            // If the player has not joined before
            if (!Preconditions.hasPlayerEverJoined(player, offlineTarget.getUniqueId()))
                  return;
      
            // If the player is not trade-blocked
            if (Preconditions.isPlayerAlreadyNotTradeBlocked(player, name))
                  return;
            
            // Unblock player
            MarketProfile profile = MarketProfile.get(player.getUniqueId());
            profile.unTradeBlockPlayer(offlineTarget.getUniqueId());
            profile.save();
            
            // Send messages and play sound
            MarketLang.TRADE_BLOCK_REMOVED.send(
                    player,
                    "%PLAYER%;" + Rank.getRank(offlineTarget.getUniqueId()).getColor() + name
            );
            SoundUtil.littleSuccess(player);
      }
      
      @Default
      @HelpCommand
      public void onHelp(CommandHelp help) {
            help.showHelp();
      }
}
